import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { HOSTS_URL } from "../constants/contsnts";

const useRating = (autoFetch: boolean) => {
  const { auth, setLoading } = useContext(AuthContext);
  const [hostRatingData, setHostRatingData] = useState<any[]>([]);
  const [error, setError] = useState<string | null>(null);

  const submitRating = async (
    hostId: string,
    rating: number,
    review: string
  ) => {
    try {
      await axiosPrivate.post(HOSTS_URL + "rate_host", {
        hostId,
        rating,
        review,
        userId: auth.id,
      });
      toast.success("Rating submitted successfully");
    } catch (error) {
      setError("Failed to submit rating. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const getRating = async (hostId: string) => {
    try {
      const response = await axiosPrivate.get(`${HOSTS_URL}get_host_rating/${hostId}`);
      
      return response.data;
    } catch (error) {
      toast.error("Failed to fetch host rating data. Please try again.");
      throw error;
    }
  };
  

  const fetchHostsRatingData = async () => {
    try {
      const response = await axiosPrivate.get(HOSTS_URL + "fetch_rating_data");
      setHostRatingData(response.data); 
    } catch (error) {
      setError("Failed to fetch host rating data. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const fetchHostRatingDataIfNeeded = () => {
      setError(null); 
      if (autoFetch) {
        fetchHostsRatingData();
      }

      if (window.location.pathname === "/host") {
        const hostId = "replaceWithActualHostId"; 
        getRating(hostId); 
      }
    };

    // Fetch data when the component mounts
    fetchHostRatingDataIfNeeded();

    // Subscribe to route changes and fetch data accordingly
    const handleRouteChange = () => {
      fetchHostRatingDataIfNeeded();
    };

    // Add the event listener for route changes
    window.addEventListener("popstate", handleRouteChange);

    // Clean up the event listener when the component unmounts
    return () => {
      window.removeEventListener("popstate", handleRouteChange);
    };
  }, [autoFetch]);

  return { hostRatingData, error, submitRating, getRating };
};

export default useRating;
