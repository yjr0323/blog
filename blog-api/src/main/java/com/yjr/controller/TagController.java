package com.yjr.controller;

import com.yjr.common.annotation.LogAnnotation;
import com.yjr.common.constant.Base;
import com.yjr.common.constant.ResultCode;
import com.yjr.common.result.Result;
import com.yjr.entity.Tag;
import com.yjr.service.TagService;
import com.yjr.vo.TagVO;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/tags")
public class TagController {


    @Autowired
    private TagService tagService;

    @GetMapping
    @LogAnnotation(module = "标签", operation = "获取所有标签")
    public Result listTags() {
        List<Tag> tags = tagService.findAll();

        return Result.success(tags);
    }

    @GetMapping("detail")
    @LogAnnotation(module = "标签", operation = "获取所有标签，详细")
    public Result listCategorysDetail() {
        List<TagVO> categorys = tagService.findAllDetail();

        return Result.success(categorys);
    }

    @GetMapping("/hot")
    @LogAnnotation(module = "标签", operation = "获取最热标签")
    public Result listHotTags() {
        int limit = 6;
        List<Tag> tags = tagService.listHotTags(limit);

        return Result.success(tags);
    }

    @GetMapping("/{id}")
    @LogAnnotation(module = "标签", operation = "根据id获取标签")
    public Result getTagById(@PathVariable("id") Integer id) {

        Result r = new Result();

        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }

        Tag tag = tagService.getTagById(id);

        r.setResultCode(ResultCode.SUCCESS);
        r.setData(tag);
        return r;
    }

    @GetMapping("/detail/{id}")
    @LogAnnotation(module = "标签", operation = "根据id获取详细标签，文章数")
    public Result getTagDetail(@PathVariable("id") Integer id) {

        Result r = new Result();

        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }

        TagVO tag = tagService.getTagDetail(id);

        r.setResultCode(ResultCode.SUCCESS);
        r.setData(tag);
        return r;
    }

    @PostMapping("/create")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "标签", operation = "添加标签")
    public Result saveTag(@Validated @RequestBody Tag tag) {

        Integer tagId = tagService.saveTag(tag);

        Result r = Result.success();
        r.simple().put("tagId", tagId);
        return r;
    }

    @PostMapping("/update")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "标签", operation = "修改标签")
    public Result updateTag(@RequestBody Tag tag) {
        Result r = new Result();

        if (null == tag.getId()) {
            r.setResultCode(ResultCode.USER_NOT_EXIST);
            return r;
        }

        Integer tagId = tagService.updateTag(tag);

        r.setResultCode(ResultCode.SUCCESS);
        r.simple().put("tagId", tagId);
        return r;
    }

    @GetMapping("/delete/{id}")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "标签", operation = "删除标签")
    public Result deleteTagById(@PathVariable("id") Integer id) {
        Result r = new Result();

        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }

        tagService.deleteTagById(id);

        r.setResultCode(ResultCode.SUCCESS);
        return r;
    }


}
