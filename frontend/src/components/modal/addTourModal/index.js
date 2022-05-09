import { Box, Button, Modal } from '@mui/material';
import React, { useEffect, useState } from 'react';
import TourForm from './TourForm';
import {useDispatch, useSelector} from 'react-redux'
import {getAllTour, addTour} from '../../../redux/reducers/listTour/action'
import { format } from 'date-fns';

const AddTourModal = ({ open, handleClose }) => {
    const dispatch = useDispatch()
    const [submit, setSubmit] = useState(false)
    const {listTour} = useSelector((store)=>store.listTour)
    console.log(listTour)
    useEffect(()=>{
        dispatch(getAllTour())
    },[])

    const style = {
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        width: 700,
        bgcolor: 'background.paper',
        boxShadow: 24,
        overflow: 'hidden',
        borderRadius:'5px',
    };

    const handleAddTour = (data)=>{
        console.log(data)
        dispatch(addTour(data))
    }
    return (
        <div className='add-tour-modal'>
            <Modal
                open={open}
                onClose={handleClose}

            >
                <Box sx={style}>
                    <div className='modal-header'>
                        <div className='modal-title'>
                            <h3>THÊM TOUR MỚI</h3>
                        </div>
                    </div>
                    <div className='modal-body'>
                        <TourForm handleAddTour={handleAddTour} submit={submit} setSubmit={setSubmit}/>
                    </div>
                    <div className='modal-footer'>
                        <div className='btn-footer'>
                            <Button variant="contained" className='btn-footer__action btn-add' onClick={()=>setSubmit(true)}>Thêm</Button>
                            <Button variant="contained" className='btn-footer__action btn-cancel' onClick={() => handleClose(false)}>Hủy</Button>
                        </div>
                    </div>
                </Box>
            </Modal>
        </div>
    );
};

export default AddTourModal;