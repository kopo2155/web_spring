package kr.ac.kopo.ctc.kopo21.board.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.ac.kopo.ctc.kopo21.board.domain.Sample;
import kr.ac.kopo.ctc.kopo21.board.repository.SampleRepository;
import kr.ac.kopo.ctc.kopo21.board.service.BoardItemService;
import kr.ac.kopo.ctc.kopo21.board.service.PaginationInfoService;
import kr.ac.kopo.ctc.kopo21.board.service.SampleService;
import kr.ac.kopo.ctc.kopo21.board.service.SampleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/sample")
public class SampleController {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private SampleService sampleService;

/*    @Autowired
    private BoardItemService boardItemService;*/

    @GetMapping("/list")
    @ResponseBody
    public List<Sample> list(Model model) {
        return sampleRepository.findAll();
    }

    @GetMapping("/noTransactional")
    @ResponseBody
    public String noTransactional(Model model) {
        sampleService.testNoTransactional();
        return "noTransactional";
    }
    @GetMapping("/Transactional")
    @ResponseBody
    public String Transactional(Model model) {
        sampleService.testTransactional();
        return "Transactional";
    }

    @GetMapping("/noCache")
    @ResponseBody
    public String noCache(Model model) {
        return sampleService.testNoCache(3L);
    }

    @GetMapping("/cache")
    @ResponseBody
    public String cache(Model model) {
        return sampleService.testCache(3L);
    }

    @GetMapping("/getParameter")
    public String getParameter(Model model, HttpServletRequest req) {
        String title = req.getParameter("title");
        model.addAttribute("title", title);
        return "sample";
    }

    @GetMapping("/requestParam")
    public String requestParam(Model model, @RequestParam String title){
        model.addAttribute("title", title);
        return "sample";
    }

    @GetMapping("/pathVariable/{title}")
    public String pathVariable(Model model, @PathVariable String title){
        model.addAttribute("title", title);
        return "sample";
    }

    @GetMapping("modelAttribute")
    public String modelAttribute(Model model, @ModelAttribute Sample sample){
        model.addAttribute("title", sample.getTitle());
        return "sample";
    }

    @PostMapping("/requestBody1")
    @ResponseBody
    public ResponseEntity<Sample> requestBody1(@RequestBody Map<String, Object> obj){
        Sample sample = new Sample();
        sample.setId(1L);
        sample.setTitle((String)obj.get("title"));
        return ResponseEntity.ok(sample);
    }


    @PostMapping("/requestBody2")
    @ResponseBody
    public Sample requestBody2(@RequestBody Sample sample){
        Sample s = new Sample();
        s.setId(1L);
        s.setTitle(sample.getTitle());
        return s;
    }

/*    @RequestMapping(value = "/hello")
    public String hello(Model model) {
        int sum = boardItemService.add(3, 5); //나중에 postrepository랑 연결

        PageRequest pageable = PageRequest.of(0,10);

        List<String> myItems = new ArrayList<>();
        myItems.add("aaa");
        myItems.add("bbb");
        myItems.add("ccc");

        model.addAttribute("name", "홍길동");
        model.addAttribute("sum", sum);
        model.addAttribute("myItems", myItems);
        return "hello";
    }*/

    @GetMapping("/selectOne")
    @ResponseBody
    public Sample selectOne(Model model, @RequestParam Long id) {
        return sampleService.selectOne(id);
    }
}
