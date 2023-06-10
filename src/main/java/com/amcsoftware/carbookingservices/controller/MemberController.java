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
        memberService.saveUserInfo(member);
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
    public Member getMembers(@RequestParam(value = "lastname") String lastName, @RequestParam(value = "email") String email) {
        return memberService.findLastNameAndEmail(lastName, email);
    }

    @ResponseBody
    @GetMapping("/{email}")
    public Member getMembers(@PathVariable("email") String email) {
        return memberService.findMemberWithEmail(email);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<CustomResponse> deleteMember(@PathVariable("email") String email) {
        memberService.deleteMember(email);
        CustomResponse response = new CustomResponse();
        response.setMessage("Member with email address:  " + email  + " has been deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<CustomResponse> updateMember(@PathVariable("email") String email, @RequestBody Member member) {
        memberService.updateMember(email, member);
        CustomResponse response = new CustomResponse();
        response.setMessage("updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
