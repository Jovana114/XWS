/* eslint-disable react-hooks/exhaustive-deps */
import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import {
  ACCOMMODATIONS_URL,
  RESERVATION_URL,
  USERS_URL,
} from "../constants/contsnts";

const useHandlingReservation = () => {
  const { auth, setLoading } = useContext(AuthContext);
  const [data, setData] = useState([]);

  const fetchDataApproved = async () => {
    try {
      const response = await axiosPrivate.get(
        ACCOMMODATIONS_URL + `list_of_pending_reservations/${auth.id}`
      );
      setData(response.data);
      setLoading(false);
    } catch (error) {
      toast.error("Failed to fetch accommodation data");
    } finally {
      setLoading(false);
    }
  };

  const fetchDataCanceled = async () => {
    try {
      const response = await axiosPrivate.get(
        USERS_URL + `reservations/${auth.id}`
      );
      setData(response.data);
      setLoading(false);
    } catch (error) {
      toast.error("Failed to fetch accommodation data");
    } finally {
      setLoading(false);
    }
  };

  const approveReservation = async (reservationId: string) => {
    try {
      await axiosPrivate.put(
        ACCOMMODATIONS_URL + `approvingRequests/${reservationId}`
      );
      toast.success("Reservation approved");
      setLoading(false);
    } catch (error) {
      toast.error("Failed to approve reservation");
    } finally {
      setLoading(false);
      fetchDataApproved();
    }
  };

  const cancelReservation = async (reservationId: string) => {
    try {
      await axiosPrivate.put(
        RESERVATION_URL + `remove_request/${reservationId}`
      );
      toast.success("Reservation canceled");
    } catch (error) {
      toast.error("Failed to cancel reservation");
    } finally {
      setLoading(false);
      fetchDataCanceled();
    }
  };

  useEffect(() => {
    if (window.location.pathname === "/reserved") {
      fetchDataCanceled();
    } else {
      fetchDataApproved();
    }
  }, []);

  return { data, approveReservation, cancelReservation };
};

export default useHandlingReservation;
