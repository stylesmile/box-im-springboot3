package com.bx.implatform.controller;

import com.bx.implatform.config.ICEServer;
import com.bx.implatform.result.Result;
import com.bx.implatform.result.ResultUtils;
import com.bx.implatform.service.IWebrtcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "webrtc视频单人通话")
@RestController
@RequestMapping("/webrtc/private")
@RequiredArgsConstructor
public class WebrtcController {

    private final IWebrtcService webrtcService;

    @Operation(method = "POST", description = "呼叫视频通话")
    @PostMapping("/call")
    public Result call(@RequestParam Long uid, @RequestBody String offer) {
        webrtcService.call(uid, offer);
        return ResultUtils.success();
    }

    @Operation(method = "POST", description = "接受视频通话")
    @PostMapping("/accept")
    public Result accept(@RequestParam Long uid, @RequestBody String answer) {
        webrtcService.accept(uid, answer);
        return ResultUtils.success();
    }


    @Operation(method = "POST", description = "拒绝视频通话")
    @PostMapping("/reject")
    public Result reject(@RequestParam Long uid) {
        webrtcService.reject(uid);
        return ResultUtils.success();
    }

    @Operation(method = "POST", description = "取消呼叫")
    @PostMapping("/cancel")
    public Result cancel(@RequestParam Long uid) {
        webrtcService.cancel(uid);
        return ResultUtils.success();
    }

    @Operation(method = "POST", description = "呼叫失败")
    @PostMapping("/failed")
    public Result failed(@RequestParam Long uid, @RequestParam String reason) {
        webrtcService.failed(uid, reason);
        return ResultUtils.success();
    }

    @Operation(method = "POST", description = "挂断")
    @PostMapping("/handup")
    public Result leave(@RequestParam Long uid) {
        webrtcService.leave(uid);
        return ResultUtils.success();
    }


    @PostMapping("/candidate")
    @Operation(method = "POST", description = "同步candidate")
    public Result forwardCandidate(@RequestParam Long uid, @RequestBody String candidate) {
        webrtcService.candidate(uid, candidate);
        return ResultUtils.success();
    }


    @GetMapping("/iceservers")
    @Operation(method = "GET", description = "获取iceservers")
    public Result<List<ICEServer>> iceservers() {
        return ResultUtils.success(webrtcService.getIceServers());
    }
}
