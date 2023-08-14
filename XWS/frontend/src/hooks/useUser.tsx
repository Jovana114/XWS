/* eslint-disable react-hooks/exhaustive-deps */
import { useContext, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { USERS_URL, EMAIL_REGEX, USER_REGEX } from "../constants/contsnts";

const useUser = () => {
  const { auth, setLoading, logout } = useContext(AuthContext);
  const [errorMsg, setErrorMsg] = useState("");

  const fetchUserData = async () => {
    try {
      //   setLoading(true);
      const response = await axiosPrivate.get(USERS_URL + auth.id);
      const userData = response.data;
      setLoading(false);
      return userData;
    } catch (error) {
      toast.error("Failed to fetch user data");
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateProfile = async (
    email: string,
    first_name: string,
    last_name: string,
    address: string
  ) => {
    const validEmailFormat = EMAIL_REGEX.test(email);
    if (!validEmailFormat) {
      toast.error("Invalid email address");
      return;
    }

    try {
      //   setLoading(true);
      await axiosPrivate.put(
        USERS_URL + `${auth.id}/data`,
        {
          email,
          first_name,
          last_name,
          address,
        },
        {
          headers: { "Content-Type": "application/json" },
        }
      );

      setLoading(false);
      toast.success("Profile updated successfully!");
    } catch (error) {
      toast.error("Failed to update profile.");
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateUsername = async (username: string) => {
    const validUsernameFormat = USER_REGEX.test(username);
    if (!validUsernameFormat) {
      toast.error(
        "Username should be 4 to 24 characters. Must begin with a letter. Letters, numbers, underscores, hyphens allowed."
      );
      return;
    }

    try {
      //   setLoading(true);
      await axiosPrivate.put(
        USERS_URL + `${auth.id}/username`,
        { username },
        {
          headers: { "Content-Type": "application/json" },
        }
      );

      if (auth.id) {
        logout();
      } else {
        toast.success("Username updated successfully!");
      }

      setLoading(false);
    } catch (error) {
      toast.error("Failed to update username.");
    } finally {
      setLoading(false);
    }
  };

  const handleUpdatePassword = async (
    oldPassword: string,
    newPassword: string,
    confirmPassword: string
  ) => {
    const authDataString = localStorage.getItem("authData");
    if (!authDataString) {
      setErrorMsg("User data not found.");
      toast.error(errorMsg);
      return;
    }

    if (newPassword !== confirmPassword) {
      toast.error("Passwords do not match.");
      return;
    }

    if (newPassword === oldPassword) {
      toast.error("New password must be different from the old password.");
      return;
    }

    try {
      //   setLoading(true);
      await axiosPrivate.put(
        USERS_URL + `${auth.id}/password`,
        { old_password: oldPassword, new_password: newPassword },
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      toast.success("Password updated successfully!");
      setErrorMsg("");
      setLoading(false);
    } catch (error: any) {
      const errorMessage =
        error.response && error.response.data
          ? error.response.data.message
          : "Failed to update password.";
      setErrorMsg(errorMessage);
      toast.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    try {
      //   setLoading(true);
      await axiosPrivate.delete(USERS_URL + auth.id);
      logout();
      toast.success("Account deleted successfully!");

      setLoading(false);
    } catch (error) {
      toast.error("Failed to delete account.");
    } finally {
      setLoading(false);
    }
  };

  return {
    handleUpdateProfile,
    handleUpdateUsername,
    handleUpdatePassword,
    handleDelete,
    fetchUserData,
  };
};

export default useUser;
