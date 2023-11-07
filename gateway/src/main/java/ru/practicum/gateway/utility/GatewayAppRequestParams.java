package ru.practicum.gateway.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GatewayAppRequestParams {
    public static final String USERID = "X-Sharer-User-Id";

    public static final String API_PREFIX_REQUESTS = "/requests";

    public static final String API_PREFIX_USERS = "/users";

    public static final String API_PREFIX_ITEMS = "/items";

    public static final String API_PREFIX_BOOKINGS = "/bookings";
}
