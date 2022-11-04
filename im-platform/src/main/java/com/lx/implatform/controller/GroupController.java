package com.lx.implatform.controller;


import com.lx.common.result.Result;
import com.lx.common.result.ResultUtils;
import com.lx.common.util.BeanUtils;
import com.lx.implatform.entity.Group;
import com.lx.implatform.service.IGroupService;
import com.lx.implatform.vo.GroupInviteVO;
import com.lx.implatform.vo.GroupMemberVO;
import com.lx.implatform.vo.GroupVO;
import com.lx.implatform.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private IGroupService groupService;

    @ApiOperation(value = "创建群聊",notes="创建群聊")
    @PostMapping("/create")
    public Result<GroupVO> createGroup(@NotEmpty(message = "群名不能为空") @RequestParam String groupName){
        return ResultUtils.success(groupService.createGroup(groupName));
    }

    @ApiOperation(value = "修改群聊信息",notes="修改群聊信息")
    @PutMapping("/modify")
    public Result<GroupVO> modifyGroup(@Valid  @RequestBody GroupVO vo){
        return ResultUtils.success(groupService.modifyGroup(vo));
    }

    @ApiOperation(value = "解散群聊",notes="解散群聊")
    @DeleteMapping("/delete/{groupId}")
    public Result<GroupVO> deleteGroup(@NotNull(message = "群聊id不能为空") @PathVariable Long groupId){
        groupService.deleteGroup(groupId);
        return ResultUtils.success();
    }

    @ApiOperation(value = "查询群聊",notes="查询单个群聊信息")
    @GetMapping("/find/{groupId}")
    public Result<GroupVO> findGroup(@NotNull(message = "群聊id不能为空") @PathVariable Long groupId){
        return ResultUtils.success(groupService.findById(groupId));
    }

    @ApiOperation(value = "查询群聊列表",notes="查询群聊列表")
    @GetMapping("/list")
    public Result<List<GroupVO>> findGroups(){
        return ResultUtils.success(groupService.findGroups());
    }

    @ApiOperation(value = "邀请进群",notes="邀请好友进群")
    @PostMapping("/invite")
    public Result invite(@Valid  @RequestBody GroupInviteVO vo){
        groupService.invite(vo);
        return ResultUtils.success();
    }

    @ApiOperation(value = "查询群聊成员",notes="查询群聊成员")
    @GetMapping("/members/{groupId}")
    public Result<List<GroupMemberVO>> findGroupMembers(@NotNull(message = "群聊id不能为空") @PathVariable Long groupId){
        return ResultUtils.success(groupService.findGroupMembers(groupId));
    }

    @ApiOperation(value = "退出群聊",notes="退出群聊")
    @DeleteMapping("/quit/{groupId}")
    public Result<GroupVO> quitGroup(@NotNull(message = "群聊id不能为空") @PathVariable Long groupId){
        groupService.quitGroup(groupId);
        return ResultUtils.success();
    }

    @ApiOperation(value = "踢出群聊",notes="将用户踢出群聊")
    @DeleteMapping("/kick/{groupId}")
    public Result<GroupVO> kickGroup(@NotNull(message = "群聊id不能为空") @PathVariable Long groupId,
                                     @NotNull(message = "用户id不能为空") @RequestParam Long userId){
        groupService.kickGroup(groupId,userId);
        return ResultUtils.success();
    }

}

