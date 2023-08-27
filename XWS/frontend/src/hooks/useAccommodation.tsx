/* eslint-disable react-hooks/exhaustive-deps */
import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { ACCOMMODATIONS_URL } from "../constants/contsnts";

const useAccomodation = (autoFetch: boolean) => {
  const { auth, setLoading } = useContext(AuthContext);
  const [data, setData] = useState<any[]>([]);
  const token = localStorage.getItem("token");

  const fetchAccommodationData = async () => {
    try {
      const response = await axiosPrivate.get(ACCOMMODATIONS_URL + "all");
      const accommodations = response.data;

      const accommodationsWithImages = await loadAccommodationImages(
        accommodations
      );

      setData(accommodationsWithImages);

      setLoading(false);
    } catch (error) {
      toast.error("Failed to fetch accommodation data");
      setLoading(false);
    }
  };

  const loadAccommodationImages = async (accommodationsToLoad: any) => {
    const updatedAccommodationsPromises = accommodationsToLoad.map(
      async (accommodation: any) => {
        try {
          const imageResponse = await axiosPrivate.get(
            ACCOMMODATIONS_URL + `images/${accommodation.id}.jpeg`,
            { responseType: "blob" }
          );

          return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onloadend = () => {
              accommodation.image = reader.result;
              resolve(accommodation);
            };
            reader.onerror = (error) => {
              console.error(
                `Error converting image for accommodation ID ${accommodation.id}:`,
                error
              );
              reject(error);
            };
            reader.readAsDataURL(imageResponse.data);
          });
        } catch (error) {
          console.error(
            `Error fetching image for accommodation ID ${accommodation.id}:`,
            error
          );
          return accommodation;
        }
      }
    );

    return Promise.all(updatedAccommodationsPromises);
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
      const createAccommodationResponse = await axiosPrivate.post(
        ACCOMMODATIONS_URL + "create/" + auth.id,
        {
          name,
          location,
          benefits,
          min_guests,
          max_guests,
        }
      );

      console.log("Accommodation created:", createAccommodationResponse);

      if (imageFile) {
        const accommodationId = createAccommodationResponse.data.id;
        const setImageURL =
          ACCOMMODATIONS_URL + `add_image/${accommodationId}/image`;
        const formData = new FormData();
        formData.append("file", imageFile);

        await axiosPrivate.put(setImageURL, formData, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
          },
        });

        console.log("Image uploaded successfully");
      }

      toast.success("Successfully created accommodation");
      fetchAccommodationData();
    } catch (error) {
      console.error("Error creating accommodation:", error);
      toast.error("Failed to create accommodation");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (autoFetch) fetchAccommodationData();
  }, []);

  return { data, fetchFilteredAccommodationData, createAccomodation };
};

export default useAccomodation;
