package com.yjr.repository.wrapper;

import com.yjr.vo.TagVO;

import java.util.List;


public interface TagWrapper {

    List<TagVO> findAllDetail();

    TagVO getTagDetail(Integer tagId);
}
