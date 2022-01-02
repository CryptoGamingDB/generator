package xyz.cgdb.records;

import java.util.List;

public record Game(
        String name,
        String slug,
        String website,
        String status,
        List<Contract> contracts) {
}
