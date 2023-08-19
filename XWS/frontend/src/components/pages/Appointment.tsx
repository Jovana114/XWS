/* eslint-disable react-hooks/exhaustive-deps */
import {
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Box,
  TextField,
  Button,
} from "@mui/material";
import { useState } from "react";
import useUser from "../../hooks/useUser";
import useAppointment from "../../hooks/useAppointment";

const Appointment = () => {
  const { accommodations } = useUser();
  const { createAppointment } = useAppointment();
  const [accommodationId, setAccommodationId] = useState("");
  const [start, setStart] = useState<string>(new Date().toISOString());
  const [end, setEnd] = useState<string>(new Date().toISOString());
  const [pricePerGuest, setPricePerGuest] = useState(0);
  const [pricePerAccommodation, setPricePerAccommodation] = useState(0);

  const handleCreateAppointment = () => {
    createAppointment(
      accommodationId,
      start + ":00",
      end + ":00",
      pricePerGuest,
      pricePerAccommodation
    );
  };

  return (
    <Box display="flex" flexDirection="column">
      <FormControl fullWidth required sx={{ width: "200px", margin: "10px 0" }}>
        <InputLabel id="accommodations-label">Accommodations</InputLabel>
        <Select
          labelId="accommodations-label"
          id="accommodations"
          value={accommodationId}
          label="Accommodations"
          onChange={(e) => setAccommodationId(e.target.value)}
        >
          {accommodations.map((accommodation: any, index: number) => (
            <MenuItem key={index} value={accommodation.id}>
              {accommodation.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <TextField
        label="Start"
        type="datetime-local"
        value={start}
        onChange={(e) => setStart(e.target.value)}
        fullWidth
        required
        sx={{ width: "200px", margin: "10px 0" }}
      />
      <TextField
        label="End"
        type="datetime-local"
        value={end}
        onChange={(e) => setEnd(e.target.value)}
        fullWidth
        required
        sx={{ width: "200px", margin: "10px 0" }}
      />
      <TextField
        label="Price Per Guest"
        type="number"
        value={pricePerGuest}
        onChange={(e) => setPricePerGuest(parseInt(e.target.value))}
        fullWidth
        required
        inputProps={{
          min: 0,
        }}
        sx={{ width: "200px", margin: "10px 0" }}
      />
      <TextField
        label="Price Per Accommodation"
        type="number"
        value={pricePerAccommodation}
        onChange={(e) => setPricePerAccommodation(parseInt(e.target.value))}
        fullWidth
        required
        inputProps={{
          min: 0,
        }}
        sx={{ width: "200px", margin: "10px 0" }}
      />
      <Button
        variant="contained"
        color="primary"
        onClick={handleCreateAppointment}
      >
        Create Appointment
      </Button>
    </Box>
  );
};

export default Appointment;
