package com.bigdata.etl.udaf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.objectinspector.*;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;

@Description(name = "collect_actions", value = "_FUNC_(x) - Collect someone's actions before 30 minutes.")
public class UDAFCollectAction extends AbstractGenericUDAFResolver {

    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters) throws SemanticException {
        //判断参数个数
        if (parameters.length != 1) {
            throw new UDFArgumentTypeException(parameters.length - 1, "Exactly one argument is excepted.");
        }

        ObjectInspector oi = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(parameters[0]);
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

            }
        }


        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            return null;
        }

        public void reset(AggregationBuffer agg) throws HiveException {

        }

        public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {

        }

        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            return null;
        }

        public void merge(AggregationBuffer agg, Object partial) throws HiveException {

        }

        public Object terminate(AggregationBuffer agg) throws HiveException {
            return null;
        }
    }
}
