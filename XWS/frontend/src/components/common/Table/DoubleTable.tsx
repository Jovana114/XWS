import React, { useState } from "react";
import Box from "@mui/material/Box";
import Collapse from "@mui/material/Collapse";
import IconButton from "@mui/material/IconButton";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import { Button } from "@mui/material";

interface CollapsibleTableProps {
  data: Array<{ [key: string]: any }>;
  columns: Array<{ key: string; text: string }>;
  collapseColumn?: string;
  collapseColumns?: Array<{ key: string; text: string; label?: string }>; // Add this prop for child table columns
  onButtonClick?: (rowData: any) => void;
}

function DoubleTable({
  data,
  columns,
  collapseColumn,
  collapseColumns,
  onButtonClick,
}: CollapsibleTableProps) {
  const [openRows, setOpenRows] = useState<string[]>([]);

  const handleRowClick = (rowKey: string) => {
    if (collapseColumn) {
      setOpenRows((prevOpenRows) =>
        prevOpenRows.includes(rowKey)
          ? prevOpenRows.filter((key) => key !== rowKey)
          : [...prevOpenRows, rowKey]
      );
    }
  };

  return (
    <TableContainer
      component={Paper}
      style={{ boxShadow: "none" }}
      className="tableRow"
    >
      <Table aria-label="collapsible table">
        <TableHead style={{ borderBottom: "1px solid rgba(0,0,0,0.1)" }}>
          <TableRow>
            {collapseColumn && <TableCell />}
            {columns.map((column) => (
              <TableCell key={column.key}>{column.text}</TableCell>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map((row, index) => (
            <React.Fragment key={index}>
              <TableRow
                sx={{ "& > *": { borderBottom: "unset" } }}
                onClick={() => handleRowClick(row.key || index.toString())}
              >
                {collapseColumn && (
                  <TableCell>
                    <IconButton aria-label="expand row" size="small">
                      {openRows.includes(row.key || index.toString()) ? (
                        <KeyboardArrowUpIcon />
                      ) : (
                        <KeyboardArrowDownIcon />
                      )}
                    </IconButton>
                  </TableCell>
                )}
                {columns.map((column) => (
                  <TableCell key={column.key}>{row[column.key]}</TableCell>
                ))}
              </TableRow>
              {collapseColumn &&
                (row[collapseColumn]?.length ?? 0) > 0 &&
                openRows.includes(row.key || index.toString()) && (
                  <TableRow>
                    <TableCell
                      style={{ paddingBottom: 0, paddingTop: 0 }}
                      colSpan={columns.length + 1}
                    >
                      <Collapse in={true} timeout="auto" unmountOnExit>
                        <Box
                          sx={{
                            width: " 100%",
                            borderLeft: "1px solid rgba(0,0,0,0.1)",
                            borderRight: "1px solid rgba(0,0,0,0.1)",
                          }}
                        >
                          <Table
                            aria-label="purchases"
                            className="custom-table"
                          >
                            <TableHead
                              style={{
                                borderBottom: "1px solid rgba(0,0,0,0.1)",
                              }}
                            >
                              <TableRow>
                                {collapseColumns &&
                                  collapseColumns.map((column) => (
                                    <TableCell key={column.key}>
                                      {column.text}
                                    </TableCell>
                                  ))}
                              </TableRow>
                            </TableHead>
                            <TableBody>
                              {row[collapseColumn]?.map(
                                (item: any, itemIndex: number) => (
                                  <TableRow
                                    key={itemIndex}
                                    style={{
                                      borderTop: "1px solid rgba(0,0,0,0.1)",
                                    }}
                                  >
                                    {collapseColumns &&
                                      collapseColumns.map((column: any) => (
                                        <TableCell
                                          key={column.key}
                                          style={{
                                            border: "none",
                                          }}
                                        >
                                          {column.key === "id" ? (
                                            <Button
                                              onClick={() =>
                                                onButtonClick &&
                                                onButtonClick(item)
                                              }
                                            >
                                              {column.label}
                                            </Button>
                                          ) : (
                                            item[column.key]
                                          )}
                                        </TableCell>
                                      ))}
                                  </TableRow>
                                )
                              )}
                            </TableBody>
                          </Table>
                        </Box>
                      </Collapse>
                    </TableCell>
                  </TableRow>
                )}
            </React.Fragment>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

export default DoubleTable;
