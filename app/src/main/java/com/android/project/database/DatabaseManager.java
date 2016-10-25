package com.android.project.database;

import com.android.project.model.Option;
import com.android.project.model.Record;

import java.util.List;

/**
 * Created by Lobster on 15.09.16.
 */
public interface DatabaseManager {

    Long save(Record record);

    Option addVote(Long recordId, Option option, String votedUser);

    List<Record> saveAll(List<Record> records);

    Record getRecordById(Long id);

}
