package rw.ac.rca.spring_boot_template.utils;


import rw.ac.rca.spring_boot_template.exceptions.BadRequestAlertException;

import java.util.UUID;

public interface Constants {
    /**
     * Default Pagination Page Number
     */
    public String DEFAULT_PAGE_NUMBER = "1";


    /**
     * Default Pagination Page Size
     */
    public String DEFAULT_PAGE_SIZE = "100";
    public UUID DEFAULT_SEARCH_UUID = null;


    /**
     * Maximum Page Size
     */
    public int MAX_PAGE_SIZE = 1000;

    public String TOKEN_TYPE = "Bearer";


    /**
     * Validate Request Page Number and Page Size
     * @param pageNumber Page Number
     * @param pageSize Page Size
     */
    public static void validatePageNumberAndSize(int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            throw new BadRequestAlertException("Page number is less than zero.");
        }

        if (pageSize > Constants.MAX_PAGE_SIZE) {
            throw new BadRequestAlertException("Page size is greater than " + Constants.MAX_PAGE_SIZE);
        }
    }
}