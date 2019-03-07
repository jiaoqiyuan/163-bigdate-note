package com.bigdata.etl.udaf;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.objectinspector.*;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import java.util.*;

@Description(name = "collect_actions", value = "_FUNC_(x) - Collect someone's actions before 30 minutes.")
public class UDAFCollectAction extends AbstractGenericUDAFResolver {

    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters) throws SemanticException {
        //判断参数个数
        if (parameters.length != 2) {
            throw new UDFArgumentTypeException(parameters.length - 1, "Two argument is excepted.");
        }

        ObjectInspector oi = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(parameters[0]);
//        ObjectInspector oi1 = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(parameters[1]);
        if (oi.getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentTypeException(0, "Argument must be PRIMITIVE, but"
                + oi.getCategory().name()
                + " was passed.");
        }

//        PrimitiveObjectInspector inputOI = (PrimitiveObjectInspector) oi;
//        if (inputOI.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.STRING) {
//            throw new UDFArgumentTypeException(0, "Argument must be String, but"
//                    + inputOI.getPrimitiveCategory().name()
//                    + " was passed.");
//        }

        return new AllActionsOfThisPeople30MinBefore();
    }

    public static class AllActionsOfThisPeople30MinBefore extends GenericUDAFEvaluator {
        private PrimitiveObjectInspector inputOI;
        private StandardListObjectInspector loi;
        private StandardListObjectInspector internalMergeOI;

        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException{
            super.init(m, parameters);
            /**
             * collect_set函数每个阶段分析
             * 1.PARTIAL1阶段，原始数据到部分聚合，在collect_set中，则是将原始数据放入set中，所以，
             * 输入数据类型是PrimitiveObjectInspector，输出类型是StandardListObjectInspector
             * 2.在其他情况，有两种情形：（1）两个set之间的数据合并，也就是不满足if条件情况下
             *（2）直接从原始数据到set，这种情况的出现是为了兼容从原始数据直接到set，也就是说map后
             * 直接到输出，没有reduce过程，也就是COMPLETE阶段
             */
            if (m == Mode.PARTIAL1) {
                inputOI = (PrimitiveObjectInspector) parameters[0];
                return ObjectInspectorFactory.getStandardListObjectInspector((PrimitiveObjectInspector) ObjectInspectorUtils.getStandardObjectInspector(inputOI));
            } else {
                if (!(parameters[0] instanceof StandardListObjectInspector)) {
                    inputOI = (PrimitiveObjectInspector) ObjectInspectorUtils.getStandardObjectInspector(parameters[0]);
                    return (StandardListObjectInspector) ObjectInspectorFactory.getStandardListObjectInspector(inputOI);
                } else {
                    internalMergeOI = (StandardListObjectInspector) parameters[0];
                    inputOI = (PrimitiveObjectInspector) internalMergeOI.getListElementObjectInspector();
                    loi = (StandardListObjectInspector) ObjectInspectorUtils.getStandardObjectInspector(internalMergeOI);
                    return loi;
                }
            }
        }

        static class MKArrayAggregationBuffer implements AggregationBuffer {
            List<Object> container = Lists.newArrayList();
        }

        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            MKArrayAggregationBuffer ret = new MKArrayAggregationBuffer();
            return ret;
        }

        public void reset(AggregationBuffer agg) throws HiveException {
            ((MKArrayAggregationBuffer) agg).container.clear();

        }

        public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {
            if (parameters == null || parameters.length != 2) {
                return;
            }
            Object key = parameters[0];
            Object value = parameters[1];
            if (key != null) {
                MKArrayAggregationBuffer myagg = (MKArrayAggregationBuffer) agg;
                putIntoList(key, myagg.container);
                putIntoList(value.toString(), myagg.container);
            }
        }

        private void putIntoList(Object key, List<Object> container) {
            Object pCopy = ObjectInspectorUtils.copyToStandardObject(key, this.inputOI);
            container.add(pCopy);
        }

        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            MKArrayAggregationBuffer myagg = (MKArrayAggregationBuffer) agg;
            List<Object> ret = Lists.newArrayList(myagg.container);
            return ret;
        }

        public void merge(AggregationBuffer agg, Object partial) throws HiveException {
            if (partial == null) {
                return;
            }
            MKArrayAggregationBuffer myagg = (MKArrayAggregationBuffer) agg;
            List<Object> partialResult = (List<Object>) internalMergeOI.getList(partial);
            for (Object ob : partialResult) {
                putIntoList(ob, myagg.container);
            }
            return;
        }

        public Object terminate(AggregationBuffer agg) throws HiveException {
            MKArrayAggregationBuffer myagg = (MKArrayAggregationBuffer) agg;

            Map<Text, Text> map = Maps.newHashMap();
            for (int i = 0; i < myagg.container.size(); i+=2) {
                String active_name = myagg.container.get(i).toString();
                String time_tag = myagg.container.get(i+1).toString();
                map.put(new Text(time_tag), new Text(active_name));
            }

            List<Map.Entry<Text, Text>> listData = Lists.newArrayList(map.entrySet());
            Collections.sort(listData, new Comparator<Map.Entry<Text, Text>>() {
                public int compare(Map.Entry<Text, Text> o1, Map.Entry<Text, Text> o2) {
                    if (Long.parseLong(o1.getKey().toString()) < Long.parseLong(o2.getKey().toString())){
                        return -1;
                    } else if (Long.parseLong(o1.getKey().toString()) == Long.parseLong(o2.getKey().toString())) {
                        return 0;
                    } else
                        return 1;
                }
            });

            List<Object> ret = Lists.newArrayList();
            int i = 1;
            for (Map.Entry<Text, Text> entry:listData) {
                ret.add(new Text(i + " -> " + entry.getValue().toString()));
                i++;
            }

            return ret;
        }


    }
}
