import * as types from './types'
import API from '../../../api/ListTourAPI'

const getAllTour = (callback = ()=>{}) => {
    return (dispatch) => {
        dispatch({type: types.GET_TOUR})
        API.getAllTour()
        // .then((response)=>response.json())
        .then((result)=>{
            console.log(result)
            if(result.status === 200){
                dispatch({
                    type: types.GET_TOUR_SUCCESS,
                    payload: {...result.data}
                })
                callback()
            }else{
                dispatch({
                    type: types.GET_TOUR_FAIL
                })
            }
        })
        .catch((error)=>{
            dispatch({
                type: types.GET_TOUR_FAIL
            })
        })
    }
}

const addTour = (data, callback=()=>{}) =>{
    console.log(data)
    return (dispatch) => {
        dispatch({type: types.ADD_TOUR})
        API.addTour(data)
        // .then((response)=>response.json())
        .then((result)=>{
            // if(result.status)
            if(result.status === 201){
                dispatch({
                    type: types.ADD_TOUR_SUCCESS,
                    payload: {...data}
                })
                callback()
            }else{
                dispatch({
                    type: types.ADD_TOUR_FAIL
                })
            }
        })
        .catch((error)=>{
            dispatch({
                type: types.ADD_TOUR_FAIL
            })
        })
    }
}

const deleteTour = (id, callback = ()=>{}) => {
    return (dispatch) => {
        dispatch({type: types.DELETE_TOUR})
        API.deleteTour(id)
        // .then((response)=>response.json())
        .then((result)=>{
            // if(result.status)
            if(result.status === 204){
                dispatch({
                    type: types.DELETE_TOUR_SUCCESS,
                    payload: {...result}
                })
                callback()
            }else{
                dispatch({
                    type: types.DELETE_TOUR_FAIL
                })
            }
        })
        .catch((error)=>{
            dispatch({
                type: types.DELETE_TOUR_FAIL
            })
        })
    }
}

export {
    getAllTour,
    addTour,
    deleteTour
}