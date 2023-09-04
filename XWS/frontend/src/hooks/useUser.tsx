/* eslint-disable react-hooks/exhaustive-deps */
import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { USERS_URL, EMAIL_REGEX } from "../constants/contsnts";

const useUser = () => {
  const { auth, logout } = useContext(AuthContext);
  const [errorMsg, setErrorMsg] = useState("");
  const [accommodations, setAccommodations] = useState<any>([]);

  const fetchUserData = async () => {
    try {
      const response = await axiosPrivate.get(
        USERS_URL + "getUserById/" + auth.id
      );
      const userData = response.data;
      return userData;
    } catch (error) {
      toast.error("Failed to fetch user data");
    }
  };

  const fetchUserAccommodations = async () => {
    try {
      const response = await axiosPrivate.get(
        USERS_URL + "accommodations/" + auth.id
      );
      setAccommodations(response.data);
    } catch (error) {
      toast.error("Failed to fetch user accommodations");
    }
  };

  const handleUpdateProfile = async (
    email: string,
    first_name: string,
    last_name: string,
    address: string,
    username: string
  ) => {
    const validEmailFormat = EMAIL_REGEX.test(email);
    if (!validEmailFormat) {
      toast.error("Invalid email address");
      return;
    }

    try {
      await axiosPrivate.put(
        USERS_URL + `${auth.id}`,
        {
          email,
          first_name,
          last_name,
          address,
          username,
        },
        {
          headers: { "Content-Type": "application/json" },
        }
      );

      toast.success("Profile updated successfully!");
    } catch (error) {
      toast.error("Failed to update profile.");
    }
  };

  const handleUpdatePassword = async (
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

    try {
      await axiosPrivate.put(
        USERS_URL + "password/" + `${auth.id}/` + newPassword
      );
      toast.success("Password updated successfully!");
      setErrorMsg("");
    } catch (error: any) {
      const errorMessage =
        error.response && error.response.data
          ? error.response.data.message
          : "Failed to update password.";
      setErrorMsg(errorMessage);
      toast.error(errorMessage);
    }
  };

  const handleDelete = async () => {
    try {
      const response = await axiosPrivate.delete(
        USERS_URL + "delete_user/" + auth.id
      );
      if (response.data == "User deleted successfully!") {
        toast.success("Account deleted successfully!");
        logout();
      } else {
        console.log(response.data.message);

        toast.info(response.data.message);
      }
    } catch (error) {
      toast.error("Failed to delete account.");
    }
  };
  useEffect(() => {
    fetchUserAccommodations();
  }, []);

  return {
    accommodations,
    handleUpdateProfile,
    handleUpdatePassword,
    handleDelete,
    fetchUserData,
  };
};

export default useUser;
