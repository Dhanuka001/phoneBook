import React from 'react'

const Header = ({toggleModal,noOfContacts}) => {
  return (
    <header className='header'>
      <div className='container'>
            <h3>Contact List ({noOfContacts})</h3>
            <button onClick= {() => toggleModal(true)} className='btn'><i className='bi bi-plus-quare'></i>Add New Contact</button>
      </div>
    </header>
  )
}

export default Header
