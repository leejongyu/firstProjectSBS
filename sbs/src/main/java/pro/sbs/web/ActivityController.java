package pro.sbs.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pro.sbs.domain.Activity;
import pro.sbs.domain.MyActivityList;
import pro.sbs.dto.ActivityCreateDto;
import pro.sbs.dto.ActivityInfoDto;
import pro.sbs.dto.ActivityUpdateDto;
import pro.sbs.dto.PostReadDto;
import pro.sbs.service.ActivityService;
import pro.sbs.service.MyActivityListService;
import pro.sbs.service.PostService;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping
public class ActivityController {

    private final PostService postService;

    private final ActivityService activityService;
    
    private final MyActivityListService myActivityListService;
    
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/team/teamNoticePost")
    public ResponseEntity<List<PostReadDto>> teamNoticePost(Integer teamId) {

        log.info("teamNoticePost()");
        log.info("id={}", teamId);
        List<PostReadDto> post = postService.read(teamId);
        log.info("teamNoticePOst List<PostReadDto> post = {}", post);
        return ResponseEntity.ok(post);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping({ "/activity/detail", "/activity/modify" })
    public void detail(Integer id, Model model) {
        log.info("detail, modify  activityId = {}", id);
        Activity activity = activityService.readIndex(id);
        log.info("detail, modify  active = {}", activity);
        model.addAttribute(activity);
        model.addAttribute(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/activity/update")
    public String update(ActivityUpdateDto dto) {
        log.info("update(dto = {})", dto);
        Integer activityId = activityService.update(dto);

        log.info("activityId = {}", activityId);

        return "redirect:/activity/detail?id=" + dto.getActivityId();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/activity/delete")
    public String delete(Integer id, RedirectAttributes attrs) {

        log.info("delete(id={})", id);

        Integer activityId = activityService.delete(id);
        attrs.addFlashAttribute("deleted activityId", activityId);
        log.info("postController delete activityId = {}", activityId);

        return "redirect:/team/teamActivity?teamId=21";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/activity/create")
    public void create(Integer id, Model model) {
        log.info("ActivityController create() teamId = {}", id);
        model.addAttribute("id", id);
    }

    /**
     * ?????? ?????? ??????
     * @param id
     * @param dto
     * @return
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/activity/create")
    public String create(Integer id, ActivityCreateDto dto) {

        log.info("ActivityController create() dto2 = {}", dto);
        Activity entity = activityService.create(dto);
        log.info("ActivityController create() entity = {}", entity);
        Integer teamId = dto.getTeamId();
        log.info("list.get(0) ={}",entity.getActivityId());
        String nickName = myActivityListService.readByUserName(entity.getUserName());
        MyActivityList entity2 = myActivityListService.createActivityCreate(teamId, entity.getActivityId(), entity.getUserName(), nickName);
        log.info("activity create()  entity = {}", entity2);
        
        log.info("id={}", teamId);

        return "redirect:/team/teamActivity?teamId=" + teamId;
    }
    
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/team/list") // ?????? URL/?????? ??????.
    public ResponseEntity<List<Activity>> home2(Model model) {
        log.info("home()");

        List<Activity> list = activityService.readByStartTime();

        model.addAttribute("list", list);

        return ResponseEntity.ok(list);
    }

    /*
    *  ????????? ?????? ????????? ?????? ?????? ???????????? ??????.
    */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/team/pastList") // ?????? URL/?????? ??????.
    public ResponseEntity<List<Activity>> home3(Integer teamId, Model model) {
        // ?????? ?????? ?????? ?????? ??????
        List<Activity> list1 = activityService.readAcTimePast(teamId);
        model.addAttribute("pastList", list1);
        return ResponseEntity.ok(list1);
    }

    /*
    *  ???????????? ?????? ?????? ???????????? ??????.
    */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/team/progressList") // ?????? URL/?????? ??????.
    public ResponseEntity<List<Activity>> home4(Integer teamId, Model model) {
        // ????????? ????????? ?????? ?????? ?????? ??????
        List<Activity> list2 = activityService.readAcTimeProgress(teamId);
        model.addAttribute("progressList", list2);
        return ResponseEntity.ok(list2);
    }

    /**
     * ??????????????? ?????? ????????? input text??? ?????? text?????? ????????? String ???????????? ????????? ???????????? ???????????? ???????????? ??????.
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/scTimeSearch")
    public ResponseEntity<List<Activity>> scTimeSearch(String startTime, Integer teamId) {

        log.info("scTimeSearch()");

        log.info("scheduleTime={}", startTime);

        List<Activity> list = new ArrayList<>();

        list = activityService.scTimeRead(startTime, teamId);

        return ResponseEntity.ok(list);
    }

    /**
     * ?????? ???????????? ??????????????? ?????? ?????? ???????????? ?????? ????????? ????????? ?????? ???????????? ??????????????? ??????.
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/activityInfo")
    public ResponseEntity<ActivityInfoDto> myActivityList(Integer activityId) {

        log.info("MyActivityList()");
        ActivityInfoDto info = activityService.myActivityListJoinMember(activityId);
        return ResponseEntity.ok(info);
    }

}
