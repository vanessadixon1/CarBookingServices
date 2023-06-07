package com.amcsoftware.carbookingservices.controller;

import com.amcsoftware.carbookingservices.customResponse.CustomResponse;
import com.amcsoftware.carbookingservices.model.Member;
import com.amcsoftware.carbookingservices.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/registration")
    public ResponseEntity<CustomResponse> registerUser(@RequestBody Member member) {
        memberService.saveMember(member);
        CustomResponse response = new CustomResponse();
        response.setMessage("successfully created");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public List<Member> getMembers() {
        return memberService.getMembers();
    }

    @ResponseBody
    @GetMapping("/")
    public Member getMembers(@RequestParam(value = "lastName") String lastName, @RequestParam(value = "email") String email) {
        return memberService.findLastNameAndEmail(lastName, email);
    }

    @ResponseBody
    @GetMapping("/{email}")
    public Member getMembers(@PathVariable("email") String email) {
        return memberService.findMemberByEmail(email);
    }

}
