package com.bx.implatform.controller;

import com.bx.implatform.dto.GroupMessageDTO;
import com.bx.implatform.result.Result;
import com.bx.implatform.result.ResultUtils;
import com.bx.implatform.service.IGroupMessageService;
import com.bx.implatform.vo.GroupMessageVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "群聊消息")
@RestController
@RequestMapping("/message/group")
@RequiredArgsConstructor
public class GroupMessageController {

    private final IGroupMessageService groupMessageService;

    @PostMapping("/send")
    @Operation(summary = "发送群聊消息", description = "发送群聊消息")
    public Result<Long> sendMessage(@Valid @RequestBody GroupMessageDTO vo) {
        return ResultUtils.success(groupMessageService.sendMessage(vo));
    }

    @DeleteMapping("/recall/{id}")
    @Operation(summary = "撤回消息", description = "撤回群聊消息")
    public Result<Long> recallMessage(@NotNull(message = "消息id不能为空") @PathVariable Long id) {
        groupMessageService.recallMessage(id);
        return ResultUtils.success();
    }


    @GetMapping("/loadMessage")
    @Operation(summary = "拉取消息", description = "拉取消息,一次最多拉取100条")
    public Result<List<GroupMessageVO>> loadMessage(@RequestParam Long minId) {
        return ResultUtils.success(groupMessageService.loadMessage(minId));
    }


    @PutMapping("/readed")
    @Operation(summary = "消息已读", description = "将群聊中的消息状态置为已读")
    public Result readedMessage(@RequestParam Long groupId) {
        groupMessageService.readedMessage(groupId);
        return ResultUtils.success();
    }


    @GetMapping("/history")
    @Operation(summary = "查询聊天记录", description = "查询聊天记录")
    public Result<List<GroupMessageVO>> recallMessage(@NotNull(message = "群聊id不能为空") @RequestParam Long groupId,
                                                      @NotNull(message = "页码不能为空") @RequestParam Long page,
                                                      @NotNull(message = "size不能为空") @RequestParam Long size) {
        return ResultUtils.success(groupMessageService.findHistoryMessage(groupId, page, size));
    }
}

