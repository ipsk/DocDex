package me.piggypiglet.docdex.documentation.index;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.Singleton;
import me.piggypiglet.docdex.config.Javadoc;
import me.piggypiglet.docdex.documentation.objects.DocumentedObject;
import me.piggypiglet.docdex.documentation.objects.metadata.TypeMetadata;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

// ------------------------------
// Copyright (c) PiggyPiglet 2020
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class DocumentationIndex {
    private final Table<String, String, DocumentedObject> docs = HashBasedTable.create();

    public void populate(@NotNull final Javadoc javadoc, @NotNull final Set<DocumentedObject> objects) {
        javadoc.getNames().forEach(name -> objects.forEach(object -> {
            switch (object.getType()) {
                case CLASS:
                case INTERFACE:
                case ANNOTATION:
                case ENUM:
                    docs.put(name.toLowerCase(), ((TypeMetadata) object.getMetadata()).getPackage() + "." + object.getName().toLowerCase(), object);
                    break;

                case METHOD:
                    break;
                case FIELD:
                    break;
                case PARAMETER:
                    break;
            }
            docs.put(name.toLowerCase(), object.getName().toLowerCase(), object);
        }));
    }

    @Nullable
    public DocumentedObject get(@NotNull final String javadoc, @NotNull final String query) {
        if (docs.isEmpty()) {
            return null;
        }

        final String lowerJavadoc = javadoc.toLowerCase();
        final String lowerQuery = query.toLowerCase();

        if (!docs.containsRow(lowerJavadoc)) {
            return null;
        }

        if (docs.row(lowerJavadoc).isEmpty()) {
            return null;
        }

        final DocumentedObject object = docs.get(lowerJavadoc, lowerQuery);

        if (object != null) {
            return object;
        }

        System.out.println(FuzzySearch.weightedRatio("block.data.attachable", "data.attachable"));
        System.out.println(FuzzySearch.weightedRatio("attachable", "data.attachable"));

        //noinspection OptionalGetWithoutIsPresent
        return docs.row(lowerJavadoc).entrySet().stream()
                .max(Comparator.comparingInt(entry -> FuzzySearch.weightedRatio(entry.getKey(), lowerQuery)))
                .map(Map.Entry::getValue)
                .get();
    }
}