import React from 'react'
import { ResponsiveAppBarGuest } from '../drawer/ResponsiveAppBarGuest'
// import BasicTableGuest from '../table/BasicTableGuest'

export default function HomeGuest() {
  document.title = "homeguest"
  return (
    <>
      <ResponsiveAppBarGuest />
      {/*<BasicTableGuest />*/}
    </>
  )
}
