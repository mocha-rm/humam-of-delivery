package com.teamnine.humanofdelivery.controller;


import com.teamnine.humanofdelivery.dto.menu.MenuRequestDto;
import com.teamnine.humanofdelivery.dto.menu.MenuResponseDto;
import com.teamnine.humanofdelivery.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("owner/menus")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // 메뉴 생성
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuRequestDto requestDto, HttpSession session) {
        MenuResponseDto responseDto = menuService.createMenu(requestDto, session);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 메뉴 수정
    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long menuId, @RequestBody MenuRequestDto requestDto,HttpSession session) {
        MenuResponseDto responseDto = menuService.updateMenu(menuId, requestDto, session);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 메뉴 삭제 (status만 변경)
    @DeleteMapping ("/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long menuId,HttpSession session) {
        String message = menuService.deleteMenu(menuId, session);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
