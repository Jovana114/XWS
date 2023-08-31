/* eslint-disable react-hooks/exhaustive-deps */
import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { APPOINTMENTS_URL } from "../constants/contsnts";

const useAppointment = () => {
  const [data, setData] = useState<any>([]);
  const { auth, setLoading } = useContext(AuthContext);

  const fetchAppointments = async () => {
    try {
      const response = await axiosPrivate.get(
        APPOINTMENTS_URL + "appointments_per_user/" + auth.id
      );
      setData(response.data);
      setLoading(false);
    } catch (error) {
      setData([]);
      toast.error("Failed to create appointment");
      setLoading(false);
    }
  };

  const getAppointmentById = async (id: string) => {
    try {
      const response = await axiosPrivate.get(APPOINTMENTS_URL + id);
      setLoading(false);
      return response.data;
    } catch (error) {
      toast.error("Failed to updated appointment");
      setLoading(false);
      return null;
    }
  };

  const createAppointment = async (
    accommodationId: string,
    start: string,
    end: string,
    price_type: string,
    price_per: string,
    auto_reservation: boolean,
    price: number
  ) => {
    try {
      await axiosPrivate.post(APPOINTMENTS_URL + "create/" + accommodationId, {
        start,
        end,
        price_type,
        price_per,
        auto_reservation,
        price,
      });
      toast.success("Appointment created successfully");
      setLoading(false);
    } catch (error) {
      toast.error("Failed to create appointment");
      setLoading(false);
    }
  };
  const modifyAppointment = async (
    appointmentId: string,
    start: string,
    end: string,
    price_type: string,
    price_per: string,
    auto_reservation: boolean,
    price: number
  ) => {
    try {
      const payload = {
        start,
        end,
        price_type,
        price_per,
        auto_reservation,
        price,
      };

      const response = await axiosPrivate.put(
        APPOINTMENTS_URL + "update/" + appointmentId,
        payload
      );

      if (response.status === 200) {
        toast.success("Appointment updated successfully");
      } else {
        toast.error("Failed to update appointment");
      }
      setLoading(false);
      fetchAppointments();
    } catch (error) {
      toast.error("An error occurred while updating appointment");
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAppointments();
  }, []);

  return {
    data,
    fetchAppointments,
    getAppointmentById,
    createAppointment,
    modifyAppointment,
  };
};

export default useAppointment;
