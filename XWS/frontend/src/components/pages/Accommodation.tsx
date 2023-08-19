import { useState } from "react";
import { Box, FormControl, TextField, Button } from "@mui/material";
import ImageUpload from "../common/ImageUpload/ImageUpload";

const Accommodation = () => {
  const [name, setName] = useState("");
  const [location, setLocation] = useState("");
  const [amenities, setAmenities] = useState("");
  const [minGuests, setMinGuests] = useState("");
  const [maxGuests, setMaxGuests] = useState("");

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    // Implement your submit logic here
  };

  return (
    <>
      <h1>Create Accommodation</h1>
      <Box
        component="form"
        sx={{
          display: "flex",
          flexDirection: "column",
          margin: "0 auto",
          alignItems: "center",
          "& .MuiTextField-root": { my: 1, width: "300px" },
        }}
        noValidate
        autoComplete="off"
        onSubmit={handleSubmit}
      >
        <FormControl variant="outlined">
          <TextField
            type="text"
            id="name"
            label="Name"
            value={name}
            required
            onChange={(e) => setName(e.target.value)}
          />
        </FormControl>
        <FormControl variant="outlined">
          <TextField
            type="text"
            id="location"
            label="Location"
            value={location}
            required
            onChange={(e) => setLocation(e.target.value)}
          />
        </FormControl>
        <FormControl variant="outlined">
          <TextField
            type="text"
            id="amenities"
            label="Amenities"
            value={amenities}
            required
            onChange={(e) => setAmenities(e.target.value)}
          />
        </FormControl>
        {/* Photo upload functionality */}
        <FormControl variant="outlined">
          <TextField
            type="number"
            id="minGuests"
            label="Min Guests"
            value={minGuests}
            required
            onChange={(e) => setMinGuests(e.target.value)}
          />
        </FormControl>
        <FormControl variant="outlined">
          <TextField
            type="number"
            id="maxGuests"
            label="Max Guests"
            value={maxGuests}
            required
            onChange={(e) => setMaxGuests(e.target.value)}
          />
        </FormControl>

        <FormControl variant="outlined">
          <ImageUpload />
        </FormControl>
        <Button type="submit" variant="contained" color="primary">
          Create Accommodation
        </Button>
      </Box>
    </>
  );
};

export default Accommodation;
