import React from 'react'
import { ResponsiveAppBarHost } from '../drawer/ResponsiveAppBarHost'
import BasicTable from '../table/BasicTable'

export default function HomeHost() {
  document.title = "homehost"
  return (
    <>
      <ResponsiveAppBarHost />
      <BasicTable />
    </>
  )
}