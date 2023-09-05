import { useState, useEffect } from "react";

const useAccommodationFilter = (initialData, accommodations) => {
  const [filteredAccommodations, setFilteredAccommodations] = useState(
    initialData
  );

  useEffect(() => {
    // Initially, set the filtered accommodations to the provided initial data
    setFilteredAccommodations(initialData);
  }, [initialData]);

  const applyFilter = (filterCriteria) => {
    // Implement your filtering logic here based on the provided criteria
    const filteredResults = filterAccommodations(filterCriteria, accommodations);

    // Update the filtered accommodations
    setFilteredAccommodations(filteredResults);
  };

  const resetFilter = () => {
    // Reset the filtered accommodations to the initial data
    setFilteredAccommodations(initialData);
  };

  return { filteredAccommodations, applyFilter, resetFilter };
};

export default useAccommodationFilter;

// Placeholder filtering function
function filterAccommodations(filterCriteria, accommodations) {
  // Implement your actual filtering logic here
  // For demonstration purposes, we'll just return accommodations as is.
  return accommodations;
}
