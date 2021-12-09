package com.foxminded.university.formatter;

import com.foxminded.university.model.Group;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class GroupFormatter implements Formatter<Group> {

    @Override
    public String print(Group group, Locale locale) {
        return Long.toString(group.getId());
    }

    @Override
    public Group parse(String id, Locale locale) throws ParseException {
        return Group.builder().id(Integer.parseInt(id)).build();
    }

}
