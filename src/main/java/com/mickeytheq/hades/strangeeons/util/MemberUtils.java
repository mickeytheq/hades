package com.mickeytheq.hades.strangeeons.util;

import ca.cgjennings.apps.arkham.project.Member;
import com.google.common.collect.Streams;
import com.mickeytheq.hades.serialise.CardIO;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MemberUtils {
    // get all members that are descendants of the given members. duplicates are removed
    public static List<Member> getAllMemberDescendants(List<Member> members) {
        return members.stream().flatMap(o -> getAllMemberDescendants(o).stream()).distinct().collect(Collectors.toList());
    }

    // get all members that are descendants of this member (including this member)
    public static List<Member> getAllMemberDescendants(Member member) {
        if (!member.isFolder())
            return Collections.singletonList(member);

        return Streams.stream(member).flatMap(o -> getAllMemberDescendants(o).stream()).collect(Collectors.toList());
    }

    public static List<Member> getAllHadesMemberDescendants(List<Member> members) {
        return getAllMemberDescendants(members).stream().filter(MemberUtils::isMemberHadesFile).collect(Collectors.toList());
    }

    public static List<Path> getAllHadesPathDescendants(List<Member> members) {
        return getAllMemberDescendants(members).stream().filter(MemberUtils::isMemberHadesFile)
                .map(o -> o.getFile().toPath())
                .collect(Collectors.toList());
    }

    // returns true if the given member looks like an .eon game component file
    // will require opening an inspection to determine the type/contents
    public static boolean isMemberEonFile(Member member) {
        return member != null && !member.isFolder() && member.getFile().getAbsolutePath().toLowerCase().endsWith(".eon");
    }

    public static boolean isMemberHadesFile(Member member) {
        return member != null && !member.isFolder() && member.getFile().getAbsolutePath().toLowerCase().endsWith("." + CardIO.HADES_FILE_EXTENSION);
    }

    public static boolean isPathHadesFile(Path path) {
        return path.getFileName().toString().endsWith("." + CardIO.HADES_FILE_EXTENSION);
    }
}
