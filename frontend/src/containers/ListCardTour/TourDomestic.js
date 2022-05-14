import React, { useEffect } from 'react';
import {useDispatch, useSelector} from 'react-redux';
import ListCard from '../../components/ListCard';
import {getAllTourDomestic} from '../../redux/reducers/user/action'

const TourDomestic = () => {
    const dispatch = useDispatch()
    const {listTourDomestic} = useSelector((store) => store.user)
    console.log(listTourDomestic)
    useEffect(()=>{
        if(listTourDomestic.length === 0) dispatch(getAllTourDomestic())
    },[listTourDomestic])
    return (
        <div className='tour-list tour-domestic'>
            {
                listTourDomestic && (
                    <ListCard data = {listTourDomestic}/>
                )
            }
        </div>
    );
};

export default TourDomestic;
