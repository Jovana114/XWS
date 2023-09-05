/* eslint-disable react-hooks/exhaustive-deps */
import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { ACCOMMODATIONS_URL, USERS_URL } from "../constants/contsnts";

const useFilter = (autoFetch: boolean) => {
    const { setLoading } = useContext(AuthContext);
    const [data, setData] = useState<any[]>([]);
    const [min] = useState(2);
    const [max] = useState(3);
    const [ben] = useState<Text>(new Text(""));


    const findByPrice = async (priceMin: number, priceMax: number) => {
        try {
            const response = await axiosPrivate.get(
              ACCOMMODATIONS_URL + "search/byPriceRating",
              {
                params: {
                  priceMin,
                  priceMax,
                },
              }
            );
      
            setData(response.data);
            setLoading(false);
          } catch (error) {
            toast.error("Failed to fetch accommodations by price rating");
            setLoading(false);
          }
    };

    const findByBenefits = async (benefits: Text) => {
        try {
            const response = await axiosPrivate.get(
              ACCOMMODATIONS_URL + "search/byBenefits",
              {
                params: {
                  benefits,
                },
              }
            );
      
            setData(response.data);
            setLoading(false);
          } catch (error) {
            toast.error("Failed to fetch accommodations by benefits");
            setLoading(false);
          }

    };

    const findHighlightedHosts = async () => {
        try {
            const response = await axiosPrivate.get(
                USERS_URL + "search/highlighthedHosts"
            );
      
            setData(response.data);
            setLoading(false);
          } catch (error) {
            toast.error("Failed to fetch highlighted hosts");
            setLoading(false);
          }
    };

    useEffect(() => {
        if (autoFetch) {
            findByPrice(min, max);
            findByBenefits(ben);
        }
      }, []);
    
      return { 
        data, 
        findByPrice,
        findByBenefits, 
        findHighlightedHosts
    };

};

export default useFilter;