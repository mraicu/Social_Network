package map.social_network.service;

import map.social_network.domain.StatusRequest;
import map.social_network.domain.entities.Request;
import map.social_network.domain.entities.User;
import map.social_network.observer.NotifyStatus;
import map.social_network.observer.Observable;
import map.social_network.observer.Observer;
import map.social_network.repository.Repository;
import map.social_network.utils.events.RequestChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestService implements Observable<RequestChangeEvent> {

    private List<Observer<RequestChangeEvent>> requestObserver = new ArrayList<>();
    private Repository<Long, User> userRepository;
    private Repository<Long, Request> requestRepository;
//    private PagingRepository<Long, Request> pagingRequestRepository;

    private int page;
    private int size;

//    private Pageable pageable;

    public RequestService(Repository<Long, User> userRepository, Repository<Long, Request> requestRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
//        this.pagingRequestRepository= pagingRequestRepository;
    }

    public Optional<Request> saveRequest(User from, User to) {
        Request request = new Request(from, to, StatusRequest.PENDING);

        Optional<Request> res = requestRepository.save(request);
        if (!res.isEmpty()) {
            notifyObservers(new RequestChangeEvent(NotifyStatus.ADD_REQUEST, request));
        }
        return res;
    }

    public Optional<Request> deleteRequest(Long id) {
        Optional<Request> request = requestRepository.findOne(id);
        Optional<Request> res = requestRepository.delete(id);

        if (res.isEmpty()) {
            notifyObservers(new RequestChangeEvent(NotifyStatus.DELETE_REQUEST, request.get()));
        }
        return res;
    }

    public Optional<Request> updateRequest(Request request) {
        Optional<Request> res = requestRepository.update(request);
        if (res.isEmpty()) {
            notifyObservers(new RequestChangeEvent(NotifyStatus.UPDATE_REQUEST, request));
        }
        return res;
    }

    public Iterable<Request> findAllService() {
        return this.requestRepository.findAll();
    }

    public Optional<Request> findOneService(Long id) {
        return this.requestRepository.findOne(id);
    }

    public List<Request> findByUserId(Long userIdTo) {
        Predicate<Request> predicate = r -> r.getTo().getId() == userIdTo;
        Iterable<Request> all= requestRepository.findAll();
        return StreamSupport.stream(requestRepository.findAll().spliterator(), false)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public void addObserver(Observer<RequestChangeEvent> e) {
        requestObserver.add(e);
    }

    @Override
    public void removeObserver(Observer<RequestChangeEvent> e) {

    }

    @Override
    public void notifyObservers(RequestChangeEvent t) {
        requestObserver.stream().forEach(o -> o.update(t));
    }

//    public void setPageSize(int size) {
//        this.size = size;
//    }

//    public void setPageable(Pageable pageable) {
//        this.pageable = pageable;
//    }
//
//    public Set<Request> getPreviousRequests() {
//        if (this.page > 0){
//            this.page--;
//        }
//        return getUsesOnThisPage(this.page);
//    }
//
//    public Set<Request> getNextRequests() {
//        long numRequests = StreamSupport.stream(findAllService().spliterator(), false).count();
//        if ((numRequests / size ) > page) {
//            this.page++;
//        }
//        return getUsesOnThisPage(this.page);
//    }

//    public Set<Request> getUsesOnThisPage(int page) {
//        this.page = page;
//        Pageable pageable = new PageableImplementation(page, size);
//        Page<Request> friendshipPage = pagingRequestRepository.findAll(pageable);
//        return friendshipPage.getContent().collect(Collectors.toSet());
//    }

}
