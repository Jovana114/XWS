import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { ACCOMMODATIONS_URL } from "../constants/contsnts";

const approvingReservation = () => {
  const { auth, setLoading } = useContext(AuthContext);
  const [data, setData] = useState([]);

  const fetchData = async () => {
    try {
      const response = await axiosPrivate.get(ACCOMMODATIONS_URL + `list_of_pending_reservations/${auth.id}`);
      setData(response.data);
      setLoading(false);
    } catch (error) {
      toast.error("Failed to fetch accommodation data");
    } finally {
      setLoading(false);
    }
  };

  const approveReservation = async (
    reservationId: string,
  ) => {
    try {
      await axiosPrivate.put(
        ACCOMMODATIONS_URL + `approvingRequests/${reservationId}`,
      );
      toast.success("Reservation approved!");
      fetchData();
    } catch (error) {
      toast.error("Failed to approve reservation");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return { data, approveReservation };
};

export default approvingReservation;
