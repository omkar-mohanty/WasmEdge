package org.wasmedge;

import org.wasmedge.enums.ValueType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FunctionTypeContext {
    private long pointer;
    private String name;

    public FunctionTypeContext(List<ValueType> paramTypes,
                               List<ValueType> returnTypes) {
        nativeInit(getTypeValues(paramTypes), getTypeValues(returnTypes));
    }

    public FunctionTypeContext(ValueType[] paramTypes, ValueType[] returnTypes) {
        nativeInit(paramTypes == null ? null : getTypeValues(Arrays.asList(paramTypes)),
                returnTypes == null ? null : getTypeValues(Arrays.asList(returnTypes)));
    }

    private FunctionTypeContext(long pointer) {
        this.pointer = pointer;
    }

    private native void nativeInit(int[] paramsTypes, int[] returnTypes);

    private int[] getTypeValues(List<ValueType> valueTypeList) {

        int[] valueTypes = new int[valueTypeList.size()];
        IntStream.range(0, valueTypeList.size())
                .forEach(i -> valueTypes[i] = valueTypeList.get(i).getValue());
        return valueTypes;
    }

    private List<ValueType> getTypeList(int[] typeArray) {
        return IntStream.range(0, typeArray.length)
                .mapToObj(i -> ValueType.parseType(typeArray[i]))
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ValueType> getParameters() {
        return getTypeList(nativeGetParameters());
    }

    private native int[] nativeGetParameters();

    public List<ValueType> getReturns() {
        return getTypeList(nativeGetReturns());
    }

    private native int[] nativeGetReturns();

    public native void delete();

    private int[] getValueTypeArray(List<WasmEdgeValue> values) {

        int[] types = new int[values.size()];

        for (int i = 0; i < values.size(); i++) {
            types[i] = values.get(i).getType().ordinal();
        }
        return types;
    }

    private WasmEdgeValue[] valueListToArray(List<WasmEdgeValue> values) {
        WasmEdgeValue[] valuesArray = new WasmEdgeValue[values.size()];
        values.toArray(valuesArray);
        return valuesArray;
    }

}
