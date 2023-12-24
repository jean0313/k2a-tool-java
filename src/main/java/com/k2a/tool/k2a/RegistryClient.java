package com.k2a.tool.k2a;

import com.k2a.tool.k2a.registry.models.Schema;

import java.util.List;

public interface RegistryClient {
    List<String> listSubjects();

    Schema getSubjectLatestSchema(String subject);
}
