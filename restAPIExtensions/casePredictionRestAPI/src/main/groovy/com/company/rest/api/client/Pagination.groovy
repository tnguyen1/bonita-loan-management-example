/*
 * Copyright (C) 2018 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.company.rest.api.client

// ⚠️ We use `page` (=p) and `size` (=c) to be more consistent with bonita Rest APIs,
// especially in returned header in response.
class Pagination {
    int page
    int size
    int total

    static Optional<Pagination> from(def body) {
        def pagination = body.pagination

        if (!pagination?.size && !pagination?.page) {
            return Optional.empty()
        }

        Optional.of(new Pagination(size: pagination.size, page: pagination.page))
    }

    // Get from parameter for elastic query
    def from() {
        page * size
    }
}
