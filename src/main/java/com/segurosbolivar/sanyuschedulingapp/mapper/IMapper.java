package com.segurosbolivar.sanyuschedulingapp.mapper;

import java.util.List;

public interface IMapper<I, O> {

    O map(I inputObject);
    List<O> map(List<I> inputObjectList);

}