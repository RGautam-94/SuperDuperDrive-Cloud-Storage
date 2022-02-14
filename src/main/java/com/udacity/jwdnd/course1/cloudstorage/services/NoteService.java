package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private UserMapper userMapper;
    private NoteMapper noteMapper;

    public NoteService(UserMapper userMapper, NoteMapper noteMapper) {
        this.userMapper = userMapper;
        this.noteMapper = noteMapper;
    }

    // note list
    public Note[] getNoteList() {
        return noteMapper.getNoteList();
    }

    // Get note
    public Note getNote(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    // Add a new note
    public void addNote(String noteTitle, String noteDescription, String Username) {
        Integer userId;
        userId = userMapper.getUserByName(Username).getUserId();
        Integer noteId = null;
        Note note = new Note(0, noteTitle, noteDescription, userId);
        noteMapper.insert(note);

    }
    // Update note
    public void updateNote(Integer noteId, String noteTitle, String noteDescription) {
        noteMapper.updateNote(noteId, noteTitle, noteDescription);
    }
    // Delete note
    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }
}
