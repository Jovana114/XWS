import { FormControl, InputLabel, Select, MenuItem, Box } from "@mui/material";
import { useState } from "react";

const Appointment = () => {
  const [accommodationId, setAccommodationId] = useState("");
  return (
    <Box>
      <FormControl fullWidth required sx={{ width: "200px", margin: "10px 0" }}>
        <InputLabel id="demo-simple-select-label">Duration</InputLabel>
        <Select
          labelId="demo-simple-select-label"
          id="daration"
          value={accommodationId}
          label="Duration"
          onChange={(e) => setAccommodationId(e.target.value)}
        >
          <MenuItem value={1}>Hotel Filip</MenuItem>
        </Select>
      </FormControl>
    </Box>
  );
};

export default Appointment;
