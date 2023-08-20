import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { ACCOMMODATIONS_URL } from "../constants/contsnts";

const useAccomodation = (autoFetch: boolean) => {
  const { auth, setLoading } = useContext(AuthContext);
  const [data, setData] = useState([]);

  const fetchAccommodationData = async () => {
    try {
      const response = await axiosPrivate.get(ACCOMMODATIONS_URL + "all");
      setData(response.data);
      setLoading(false);
    } catch (error) {
      toast.error("Failed to fetch accommodation data");
    } finally {
      setLoading(false);
    }
  };

  const fetchData = async () => {
    try {
      const response = await axiosPrivate.get(ACCOMMODATIONS_URL + "all");
      setData(response.data);
      setLoading(false);
    } catch (error) {
      toast.error("Failed to fetch accommodation data");
    } finally {
      setLoading(false);
    }
  };

  const fetchFilteredAccommodationData = async (
    location: string,
    numGuests: number,
    start: string,
    end: string
  ) => {
    try {
      const response = await axiosPrivate.get(
        ACCOMMODATIONS_URL + "search/accommodations",
        {
          params: {
            location,
            numGuests,
            start,
            end,
          },
        }
      );
      setData(response.data);
      setLoading(false);
    } catch (error) {
      toast.error("Failed to fetch accommodation data");
    } finally {
      setLoading(false);
    }
  };

  const createAccomodation = async (
    name: string,
    location: string,
    benefits: string,
    min_guests: number,
    max_guests: number,
    imageFile: File
  ) => {
    try {

      await axiosPrivate.post(
        ACCOMMODATIONS_URL + "create/" + auth.id,
        {
          name,
          location,
          benefits,
          min_guests,
          max_guests,
        }
      ).then((response: any) => {
        console.log("response:", response);
        appendImage(response.data.id ,imageFile)
      })

      toast.success("Successfully created accommodation");
      fetchAccommodationData();

    } catch (error) {
      console.error("Error Creating Accommodation:", error);
      toast.error("Failed to create accommodation");
    } finally {
      setLoading(false);
    }
  };

  const appendImage = async (url: string, imageFile: any) => {
    try {
      setLoading(true)
      const setImageURL = ACCOMMODATIONS_URL + `add_image/${url}/image`
      const formData = new FormData();
      formData.append("file", imageFile);

      console.log("formData:", formData.get("file"));

      await axiosPrivate.put(setImageURL, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      
      setLoading(false);
    } catch (error) {
      toast.error("Appending image failed");
    } finally {
      setLoading(false);
    }
  };


  useEffect(() => {
    if(autoFetch)
      fetchAccommodationData();
  }, []);

  return { data, fetchFilteredAccommodationData, createAccomodation };
};

export default useAccomodation;