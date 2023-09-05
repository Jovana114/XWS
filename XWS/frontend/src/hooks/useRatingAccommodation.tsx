import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { RATE_ACCOMMODATIONS_URL } from "../constants/contsnts";

const useRatingAccommodation = (autoFetch: boolean) => {
  const { auth, setLoading } = useContext(AuthContext);
  const [accommodationRatingData, setaccommodationRatingData] = useState<any[]>([]);
  const [error, setError] = useState<string | null>(null);

  const submitRating = async (
    accommodationId: string,
    rating: number
  ) => {
    try {
      await axiosPrivate.post(RATE_ACCOMMODATIONS_URL + "rate_accommodation", {
        accommodationId,
        rating,
        userId: auth.id,
      });
      toast.success("Rating submitted successfully");
    } catch (error) {
      setError("Failed to submit rating. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const getRating = async (accommodationId: string) => {
    try {
      const response = await axiosPrivate.get(`${RATE_ACCOMMODATIONS_URL}get_accommodation_rating/${accommodationId}`);
      return response.data;
    } catch (error) {
      toast.error("Failed to fetch accommodation rating data. Please try again.");
      throw error;
    }
  };
  

  const fetchaccommodationsRatingData = async () => {
    try {
      const response = await axiosPrivate.get(RATE_ACCOMMODATIONS_URL + "fetch_rating_data");
      setaccommodationRatingData(response.data); 
    } catch (error) {
      setError("Failed to fetch accommodation rating data. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const fetchAccommodationRatingDataIfNeeded = () => {
      setError(null); 
      if (autoFetch) {
        fetchaccommodationsRatingData();
      }

      if (window.location.pathname === "/accommodation") {
        const accommodationId = "replaceWithActualAccommodationId"; 
        getRating(accommodationId); 
      }
    };

    // Fetch data when the component mounts
    fetchAccommodationRatingDataIfNeeded();

    // Subscribe to route changes and fetch data accordingly
    const handleRouteChange = () => {
        fetchAccommodationRatingDataIfNeeded();
    };

    // Add the event listener for route changes
    window.addEventListener("popstate", handleRouteChange);

    // Clean up the event listener when the component unmounts
    return () => {
      window.removeEventListener("popstate", handleRouteChange);
    };
  }, [autoFetch]);

  return { accommodationRatingData, error, submitRating, getRating };
};

export default useRatingAccommodation;
