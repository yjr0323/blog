package com.yjr.repository.wrapper;

import java.util.List;


import com.yjr.vo.TagVO;

/**
 * @author shimh
 * <p>
 * 2018年1月25日
 */
public interface TagWrapper {

    List<TagVO> findAllDetail();

    TagVO getTagDetail(Integer tagId);
}
