    package com.kaptureevents.KaptureEvents.service;

    import com.kaptureevents.KaptureEvents.entity.Society;
    import com.kaptureevents.KaptureEvents.model.SocietyModel;
    import com.kaptureevents.KaptureEvents.repository.SocietyRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

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

            societyRepository.save(society);
        }
    }

