export const USER_REGEX = /^[A-z][A-z0-9-_]{3,23}$/;
export const EMAIL_REGEX = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/;

export const BASE_URL = "http://localhost:8085/api";
export const USERS_URL = "/user/";
export const ACCOMMODATIONS_URL = "/accommodation/";
export const APPOINTMENTS_URL = "/appointments/";
export const RESERVATION_URL = "/reservation/";
export const HOSTS_URL = "/rate_hosts/{reservation_id}/{guest_id}/{host_id}/{rating}";
export const RATE_ACCOMMODATIONS_URL = "/rate_accommodation"

// Add other endpoints as needed
export const LOGIN_URL = "/auth/signin";
export const REGISTER_URL = "/auth/signup";

// Add other constants as needed
