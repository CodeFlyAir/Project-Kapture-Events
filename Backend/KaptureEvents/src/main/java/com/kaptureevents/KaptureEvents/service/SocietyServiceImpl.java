    package com.kaptureevents.KaptureEvents.service;

    import com.kaptureevents.KaptureEvents.entity.Society;
    import com.kaptureevents.KaptureEvents.model.SocietyModel;
    import com.kaptureevents.KaptureEvents.repository.SocietyRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Service;

    import java.util.Optional;

    @Service
    public class SocietyServiceImpl implements SocietyService{

        @Autowired
        private  SocietyRepository societyRepository;
        @Override
        public void registerSociety(SocietyModel societyModel) {
            Society society = new Society();

            society.setSocietyName(societyModel.getSocietyName());
            society.setEmailId(societyModel.getEmailId());
            society.setContact(societyModel.getContact());

            societyRepository.save(society); //saving to DB
        }

        //Getting society details from DB
        @Override
        public ResponseEntity<Society> societyProfile(Long id) {
            Optional<Society> societyOptional = societyRepository.findById(Long.valueOf(id));

            if(societyOptional.isPresent()){
                Society society = societyOptional.get();
                return new ResponseEntity<>(society, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }


    }

