package com.dku.algorithmsite.controller;

import com.dku.algorithmsite.config.auth.PrincipalDetail;
import com.dku.algorithmsite.dto.TeamDto;
import com.dku.algorithmsite.service.S3Service;
import com.dku.algorithmsite.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
public class TeamController {

    private S3Service s3Service;
    private TeamService teamService;

    @GetMapping("/team")
    public String group(Model model,  @AuthenticationPrincipal PrincipalDetail principal){
        List<TeamDto> teamList = teamService.getTeamList();
        model.addAttribute("teamList", teamList);
        model.addAttribute("principal",principal);
        if(principal != null)
            model.addAttribute("boj_username",principal.getBoj_username());

        return "group/TeamInformation";
    }

    @GetMapping("/gallery")
    public String dispWrite(Model model,@AuthenticationPrincipal PrincipalDetail principal) {
        model.addAttribute("principal",principal);
        if(principal != null)
            model.addAttribute("boj_username",principal.getBoj_username());
        return "group/MakingTeam.html";
    }

    @PostMapping("/team")
    public String execWrite(TeamDto teamDto, MultipartFile file) throws IOException {
        String imgPath = s3Service.upload(file);
        teamDto.setImage_url(imgPath);

        teamService.savePost(teamDto);
        return "redirect:/team";
    }
    @GetMapping("/team/{id}")
    public String detail(@PathVariable String id, Model model, @AuthenticationPrincipal PrincipalDetail principal) {
        model.addAttribute("team", teamService.detail(id));
        model.addAttribute("principal",principal);
        if(principal != null)
            model.addAttribute("boj_username",principal.getBoj_username());
        return "chatting/chat";
    }
}
