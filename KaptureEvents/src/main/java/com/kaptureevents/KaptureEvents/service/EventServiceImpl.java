package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.dto.FileDto;
import com.kaptureevents.KaptureEvents.entity.*;
import com.kaptureevents.KaptureEvents.model.*;
import com.kaptureevents.KaptureEvents.repository.EventApprovalRequestRepository;
import com.kaptureevents.KaptureEvents.repository.EventRepository;
import com.kaptureevents.KaptureEvents.repository.SocietyRepository;
import com.kaptureevents.KaptureEvents.repository.StudentEventRegistrationRepository;
import com.kaptureevents.KaptureEvents.utils.DataBucketUtil;
import com.kaptureevents.KaptureEvents.utils.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private EventApprovalRequestRepository eventApprovalRequestRepository;

    @Autowired
    private DataBucketUtil dataBucketUtil;

    @Autowired
    private StudentEventRegistrationRepository studentEventRegistrationRepository;

    @Override
    public ResponseEntity<List<Events>> getEvents() {
        try {
            return ResponseEntity.ok(eventRepository.findAll());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<EventPreviewModel>> getEventsPreview() {
        try {
            return ResponseEntity.ok(convertToEventPreviews(eventRepository.findAll()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<EventPreviewModel>> getEventsForHomeWithFilter(String filters) {
        try {
            Date date = new Date(System.currentTimeMillis());

            if ("today".equalsIgnoreCase(filters)) {
                Optional<List<Events>> eventsOptional = eventRepository.findByStartDateEquals(date);
                return responseHelper(eventsOptional);

            } else if ("tomorrow".equalsIgnoreCase(filters)) {
                java.util.Date utilDate = new java.util.Date(date.getTime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(utilDate);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                date = new Date(calendar.getTime().getTime());
                Optional<List<Events>> eventsOptional = eventRepository.findByStartDateEquals(date);
                return responseHelper(eventsOptional);

            } else if ("this-month".equalsIgnoreCase(filters)) {
                Calendar endOfMonth = Calendar.getInstance();
                endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date endDate = new Date(endOfMonth.getTimeInMillis());
                Optional<List<Events>> eventsOptional = eventRepository.findByStartDateBetween(date, endDate);
                return responseHelper(eventsOptional);

            } else {
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResponseEntity<List<EventPreviewModel>> responseHelper(Optional<List<Events>> eventsOptional) {
        if (eventsOptional.isPresent()) {
            List<Events> events = eventsOptional.get();
            if (events.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                List<EventPreviewModel> eventPreviews = convertToEventPreviews(events);
                return ResponseEntity.ok(eventPreviews);
            }
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    private List<EventPreviewModel> convertToEventPreviews(List<Events> events) {
        List<EventPreviewModel> eventPreviews = new ArrayList<>();
        for (Events event : events) {
            EventPreviewModel eventPreview = new EventPreviewModel(
                    event.getEvent_id(),
                    event.getName(),
                    event.getStartDate(),
                    event.getEndDate(),
                    event.getOrganizerName(),
                    event.getThumbnail()
            );
            eventPreviews.add(eventPreview);
        }
        return eventPreviews;
    }


    @Override
    public ResponseEntity<List<SubEventsModel>> addNewSubEvent(UUID eventName, SubEventsModel subEventsModel) {
        Optional<Events> eventsOptional = eventRepository.findById(eventName);
        Events events;
        if (eventsOptional.isPresent()) {
            events = eventsOptional.get();
        } else
            return ResponseEntity.notFound().build();

        // Get event start and end dates
        Date eventStartDate = events.getStartDate();
        Date eventEndDate = events.getEndDate();

        // Check if sub event date falls between start and end date of the event
        Date subEventDate = subEventsModel.getDate();
        if (subEventDate != null && !isDateWithinRange(subEventDate, eventStartDate, eventEndDate)) {
            log.error("Sub event date should be between start and end date of event");
            return ResponseEntity.badRequest().build();
        }
        List<SubEventsModel> subEventsModelList;
        subEventsModelList = events.getSubEvent();
        SubEventsModel subEventsModel1 = new SubEventsModel();

        subEventsModel1.setName(subEventsModel.getName());
        subEventsModel1.setDesc(subEventsModel.getDesc());
        subEventsModel1.setDate(subEventsModel.getDate());
        subEventsModel1.setTime(subEventsModel.getTime());
        subEventsModel1.setVenue(subEventsModel.getVenue());

        subEventsModelList.add(subEventsModel1);
        events.setSubEvent(subEventsModelList);
        eventRepository.save(events);
        return ResponseEntity.ok(subEventsModelList);
    }
    private boolean isDateWithinRange(Date date, Date startDate, Date endDate) {
        return !(date.before(startDate) || date.after(endDate));
    }

    @Override
    public ResponseEntity<List<SubEventsModel>> deleteSubEvent(UUID eventName, SubEventsModel subEventsModel) {
        Optional<Events> eventsOptional = eventRepository.findById(eventName);
        Events events;
        if (eventsOptional.isPresent()) {
            events = eventsOptional.get();
        } else
            return ResponseEntity.notFound().build();

        List<SubEventsModel> subEventsModelList;
        subEventsModelList = events.getSubEvent();
        try {
            if (subEventsModelList != null) {
                Optional<SubEventsModel> subEventsModelOptional = subEventsModelList.stream().filter(event -> event.getName().equals(subEventsModel.
                        getName())).findFirst();

                if (subEventsModelOptional.isPresent()) {
                    subEventsModelList.remove(subEventsModelOptional.get());
                    events.setSubEvent(subEventsModelList);
                    eventRepository.save(events);
                    return ResponseEntity.ok(subEventsModelList);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.status(new ErrorResponse("No Sub Events Found", HttpStatus.NOT_FOUND).getStatus()).body(null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<UpdateModel>> addUpdate(UUID eventName, String updateMessage) {
        Optional<Events> eventsOptional = eventRepository.findById(eventName);
        Events events;
        if (eventsOptional.isPresent()) {
            events = eventsOptional.get();
        } else {
            return ResponseEntity.notFound().build();
        }

        List<UpdateModel> updateModelList = events.getUpdates();
        if(updateModelList==null){
            updateModelList=new ArrayList<>();
        }
        UpdateModel updateModel = new UpdateModel();

        updateModel.setDate(new java.util.Date());
        updateModel.setMessage(updateMessage);

        updateModelList.add(updateModel);
        events.setUpdates(updateModelList);
        eventRepository.save(events);

        return ResponseEntity.ok(updateModelList);
    }

    @Override
    public ResponseEntity<SocialMediaLinksModel> addSocialMediaLinks(UUID eventName, SocialMediaLinksModel socialMediaLinksModel) {
        Optional<Events> eventsOptional = eventRepository.findById(eventName);
        Events events;
        if (eventsOptional.isPresent()) {
            events = eventsOptional.get();
        } else
            return ResponseEntity.notFound().build();
        SocialMediaLinksModel socialMediaLinks = new SocialMediaLinksModel();

        socialMediaLinks.setInstagram(socialMediaLinksModel.getInstagram());
        socialMediaLinks.setFacebook(socialMediaLinksModel.getFacebook());
        socialMediaLinks.setOther(socialMediaLinksModel.getOther());

        events.setSocialMedia(socialMediaLinksModel);
        eventRepository.save(events);
        return ResponseEntity.ok(socialMediaLinks);

    }


    //saving event to DB
    @Override
    public ResponseEntity<Events> registerEvents(EventModel eventModel, MultipartFile thumbnail, String emailId) {
        try {

            Society societyId;
            // Validate start and end dates
            Date startDate = eventModel.getStartDate();
            Date endDate = eventModel.getEndDate();

            if (startDate != null && endDate != null && endDate.after(startDate) && startDate.after(new Date(System.currentTimeMillis()))) {
                Optional<Society> societyOptional = societyRepository.findByEmailId(emailId);

                if (societyOptional.isPresent())
                    societyId = societyOptional.get();
                else
                    return ResponseEntity.notFound().build();

                Events events = new Events();

                events.setContact(new ArrayList<>());
                events.setSponsors(new ArrayList<>());
                events.setSpecialGuest(new ArrayList<>());
                events.setUpdates(new ArrayList<>());

                //setting the accepted event details to Events object
                events.setName(eventModel.getName());
                events.setStartDate(eventModel.getStartDate());
                events.setEndDate(eventModel.getEndDate());
                events.setContact(eventModel.getContact());
                events.setDescription(eventModel.getDescription());
                events.setAdditionalDetails(eventModel.getAdditionalDetails());
                events.setSocietyId(societyId);
                events.setSponsors(eventModel.getSponsors());
                events.setSpecialGuest(eventModel.getSpecialGuest());
                events.setSubEvent(eventModel.getSubEvent());
                events.setUpdates(eventModel.getUpdates());
                EventStatusModel statusModel = new EventStatusModel();
                statusModel.setStatus(EventStatusModel.approvalStatus.pending);
                statusModel.setDate(new java.util.Date());

                List<EventStatusModel> statusModelList = new ArrayList<>();
                statusModelList.add(statusModel);
                events.setEventStatus(statusModelList);

                events.setAdditionalDetails(new EventAdditionalDetailsModel());
                events.setSocialMedia(eventModel.getSocialMedia());
                events.setThumbnail(dataBucketUtil.uploadFile(thumbnail));
                events.setOrganizerName(societyOptional.get().getSocietyName());

                Events dbEvent = eventRepository.save(events);

                EventApprovalRequest eventApprovalRequest = new EventApprovalRequest();
                eventApprovalRequest.setEventId(dbEvent.getEvent_id());
                eventApprovalRequest.setStatus(EventStatusModel.approvalStatus.pending);
                eventApprovalRequestRepository.save(eventApprovalRequest);

                return ResponseEntity.ok(dbEvent);
            }
            else {
                // Invalid start or end date, return bad request
                log.error("Invalid");
                //return ResponseEntity.badRequest().build();
                // return new ErrorResponse("Invalid Start Date and End Date",HttpStatus.BAD_REQUEST);
                return ResponseEntity.status(new ErrorResponse("Invalid Start Date and End Date",HttpStatus.BAD_REQUEST).getStatus()).body(null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    //get event from DB
    @Override
    public ResponseEntity<Events> eventProfile(UUID eventId) {
        Optional<Events> eventsOptional = eventRepository.findById(eventId);
        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();
            return new ResponseEntity<>(events, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity<List<EventContactModel>> addEventContact(
            EventContactModel eventContactModel, UUID eventId, MultipartFile file) {
        Optional<Events> eventsOptional = eventRepository.findById(eventId);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            List<EventContactModel> contactList = events.getContact();

            if (contactList == null) {
                contactList = new ArrayList<>();
            }
            // Check if the contact number already exists in the list
            if (contactList.stream()
                    .anyMatch(contact -> contact.getContact()
                            .equals(eventContactModel.getContact()))) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            try {
                eventContactModel.setImage(dataBucketUtil.uploadFile(file));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }

            contactList.add(eventContactModel);

            // Update the contact list in the Events object
            events.setContact(contactList);
            eventRepository.save(events);

            return ResponseEntity.ok(contactList);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<Student>> findAllStudentsRegisteredForEvent(String eventId) {
        // Retrieve the event from the database
        Optional<Events> optionalEvent = eventRepository.findById(UUID.fromString(eventId));
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Events event = optionalEvent.get();

        // Retrieve the registered students for the event from the repository
        List<StudentEventRegistration> registrations = studentEventRegistrationRepository.findByEvent(event);

        // Extract the students from the registrations
        List<Student> registeredStudents = registrations.stream()
                .map(StudentEventRegistration::getStudent)
                .collect(Collectors.toList());

        // Return a ResponseEntity with the list of registered students
        return ResponseEntity.ok(registeredStudents);
    }


    @Override
    public ResponseEntity<List<EventContactModel>> deleteEventContact(UUID eventId, Long contactNumber) {
        Optional<Events> eventsOptional = eventRepository.findById(eventId);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            // Get the list of contacts from the event
            List<EventContactModel> contactList = events.getContact();

            if (contactList != null) {
                Optional<EventContactModel> contactToRemoveOptional = contactList.stream()
                        .filter(contact -> contact.getContact().equals(contactNumber))
                        .findFirst();

                try {
                    if (contactToRemoveOptional.isPresent() &&
                            dataBucketUtil.deleteFile(
                                    contactToRemoveOptional
                                            .get()
                                            .getImage()
                                            .getFileName())) {
                        contactList.remove(contactToRemoveOptional.get());
                        events.setContact(contactList);

                        eventRepository.save(events);
                        return ResponseEntity.ok(contactList);
                    } else {
                        return ResponseEntity.status(
                                        new ErrorResponse("Unable to Delete", HttpStatus.NOT_FOUND)
                                                .getStatus())
                                .body(null);
                    }
                } catch (Exception e) {
                    return ResponseEntity.internalServerError().build();
                }
            }
        }
        return ResponseEntity.status(
                        new ErrorResponse("Event Not Present", HttpStatus.BAD_REQUEST)
                                .getStatus())
                .body(null);
    }

    //delete from DB
    @Override
    public ResponseEntity<Boolean> deleteEvent(UUID eventId) {
        Optional<Events> events = eventRepository.findById(eventId);
        if (events.isPresent()) {
            eventRepository.deleteById(eventId);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> editTeamFormationGuidelines(UUID name, String guidelines) {
        Optional<Events> eventsOptional = eventRepository.findById(name);
        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            EventAdditionalDetailsModel additionalDetails = events.getAdditionalDetails();

            if (additionalDetails == null)
                additionalDetails = new EventAdditionalDetailsModel();

            additionalDetails.setTeamFormationGuidelines(guidelines);
            events.setAdditionalDetails(additionalDetails);

            eventRepository.save(events);
            return ResponseEntity.ok(guidelines);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> editRewards(UUID eventName, String rewards) {
        Optional<Events> eventsOptional = eventRepository.findById(eventName);
        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            EventAdditionalDetailsModel additionalDetails = events.getAdditionalDetails();

            if (additionalDetails == null)
                additionalDetails = new EventAdditionalDetailsModel();

            additionalDetails.setRewards(rewards);
            events.setAdditionalDetails(additionalDetails);

            eventRepository.save(events);
            return ResponseEntity.ok(rewards);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> editEligibilityCriteria(UUID eventName, String eligibilityCriteria) {
        Optional<Events> eventsOptional = eventRepository.findById(eventName);
        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            EventAdditionalDetailsModel additionalDetails = events.getAdditionalDetails();

            if (additionalDetails == null)
                additionalDetails = new EventAdditionalDetailsModel();

            additionalDetails.setEligibilityCriteria(eligibilityCriteria);
            events.setAdditionalDetails(additionalDetails);

            eventRepository.save(events);
            return ResponseEntity.ok(eligibilityCriteria);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<FileDto>> addResource(UUID eventName, MultipartFile file) {
        Optional<Events> eventsOptional = eventRepository.findById(eventName);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();
            EventAdditionalDetailsModel additionalDetails = events.getAdditionalDetails();

            List<FileDto> resources = additionalDetails.getResources();

            if (resources == null) {
                resources = new ArrayList<>();
            }

            try {
                resources.add(dataBucketUtil.uploadFile(file));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }

            additionalDetails.setResources(resources);
            events.setAdditionalDetails(additionalDetails);
            eventRepository.save(events);

            return ResponseEntity.ok(resources);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<FileDto>> deleteResource(UUID eventName, String fileName) {
        Optional<Events> eventsOptional = eventRepository.findById(eventName);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();
            EventAdditionalDetailsModel additionalDetails = events.getAdditionalDetails();

            List<FileDto> resources = additionalDetails.getResources();

            if (resources == null) {
                return ResponseEntity.internalServerError().build();
            }

            Iterator<FileDto> iterator = resources.iterator();
            while (iterator.hasNext()) {
                FileDto fileDto = iterator.next();

                if (fileDto.getFileName().equals(fileName)) {
                    try {
                        if (dataBucketUtil.deleteFile(fileName)) {
                            iterator.remove();

                            additionalDetails.setResources(resources);
                            events.setAdditionalDetails(additionalDetails);

                            eventRepository.save(events);

                            return ResponseEntity.ok(resources);
                        } else {
                            return ResponseEntity.internalServerError().build();
                        }
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                }
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<SponsorsModel>> addSponsor(UUID eventId, MultipartFile file) {
        try {
            Optional<Events> eventsOptional = eventRepository.findById(eventId);

            if (eventsOptional.isPresent()) {
                Events events = eventsOptional.get();
                List<SponsorsModel> sponsors = events.getSponsors();

                if (sponsors == null) {
                    sponsors = new ArrayList<>();
                }

                SponsorsModel sponsorsModel = new SponsorsModel();
                FileDto uploadedFile = dataBucketUtil.uploadFile(file);

                if (uploadedFile != null) {
                    sponsorsModel.setSponsor(uploadedFile);
                    sponsors.add(sponsorsModel);
                    events.setSponsors(sponsors);
                    eventRepository.save(events);
                    return ResponseEntity.ok(sponsors);
                } else {
                    return ResponseEntity.internalServerError().build();
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);

        }
        return ResponseEntity.internalServerError().build();
    }


    @Override
    public ResponseEntity<List<SponsorsModel>> deleteSponsor(UUID eventId, String fileName) {
        Optional<Events> eventsOptional = eventRepository.findById(eventId);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();
            List<SponsorsModel> sponsorsList = events.getSponsors();

            if (sponsorsList == null) {
                return ResponseEntity.noContent().build();
            }

            Iterator<SponsorsModel> iterator = sponsorsList.iterator();
            while (iterator.hasNext()) {
                FileDto fileDto = iterator.next().getSponsor();
                if (fileDto.getFileName().equals(fileName)) {
                    try {
                        if (dataBucketUtil.deleteFile(fileName)) {
                            iterator.remove();

                            events.setSponsors(sponsorsList);
                            eventRepository.save(events);

                            return ResponseEntity.ok(sponsorsList);
                        } else {
                            return ResponseEntity.internalServerError().build();
                        }
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                }
            }

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<SpecialGuestModel>> addSpecialGuest(UUID eventId, SpecialGuestModel specialGuestModel, MultipartFile image) {
        Optional<Events> eventsOptional = eventRepository.findById(eventId);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            List<SpecialGuestModel> specialGuestModelList = events.getSpecialGuest();
            if (specialGuestModelList == null) {
                specialGuestModelList = new ArrayList<>();
            }

            // Check if the special guest already exists in the list
            if (specialGuestModelList.stream()
                    .anyMatch(guest -> guest.getName()
                            .equals(specialGuestModel.getName()))) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            try {
                specialGuestModel.setImage(dataBucketUtil.uploadFile(image));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
            specialGuestModelList.add(specialGuestModel);
            events.setSpecialGuest(specialGuestModelList);
            eventRepository.save(events);

            return ResponseEntity.ok(specialGuestModelList);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<SpecialGuestModel>> deleteSpecialGuest(UUID eventId, String name, String fileName) {
        Optional<Events> eventsOptional = eventRepository.findById(eventId);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();
            List<SpecialGuestModel> specialGuestModelList = events.getSpecialGuest();

            if (specialGuestModelList == null) {
                return ResponseEntity.noContent().build();
            }

            Iterator<SpecialGuestModel> iterator = specialGuestModelList.iterator();
            while (iterator.hasNext()) {
                SpecialGuestModel guestModel = iterator.next();
                if (guestModel.getName().equals(name) && guestModel.getImage().getFileName().equals(fileName)) {
                    try {
                        if (dataBucketUtil.deleteFile(guestModel.getImage().getFileName())) {
                            iterator.remove();

                            events.setSpecialGuest(specialGuestModelList);
                            eventRepository.save(events);

                            return ResponseEntity.ok(specialGuestModelList);
                        } else {
                            return ResponseEntity.internalServerError().build();
                        }
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
}
