package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/note")
public class NoteController {
    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUserByName(userName);
        return user.getUserId();
    }
    // Get note- path variable noteID
    @GetMapping(value = "/note/{noteId}")
    public Note getNote(@PathVariable Integer noteId) {
        return noteService.getNote(noteId);
    }

    //Add or update note
    @PostMapping("add-note")
    public String addOrUpdateNote(@ModelAttribute("newFile") NoteForm noteForm, Authentication authentication, Model model){
     Integer userId = getUserId(authentication);
     String noteTitle = noteForm.getNoteTitle();
     String noteId = noteForm.getNoteId();
     String userName = authentication.getName();
     String noteDescription = noteForm.getNoteDescription();
     if (noteId.isEmpty()) {
            noteService.addNote(noteTitle, noteDescription,userName);
        } else {
            Note updateNote = getNote(Integer.parseInt(noteId)) ;
            noteService.updateNote(updateNote.getNoteId(), noteTitle, noteDescription);
        }
    model.addAttribute("notes", noteService.getNoteList());
    model.addAttribute("result", "success");
    return "result";
    }
    // Delete note
    @GetMapping(value="/delete-note/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, Model model){
        noteService.deleteNote(noteId);
        model.addAttribute("notes", noteService.getNoteList());
        model.addAttribute("result", "success");
        return "result";
    }

}
