/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { FyPv6TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { UserProfileDetailComponent } from '../../../../../../main/webapp/app/entities/user-profile/user-profile-detail.component';
import { UserProfileService } from '../../../../../../main/webapp/app/entities/user-profile/user-profile.service';
import { UserProfile } from '../../../../../../main/webapp/app/entities/user-profile/user-profile.model';

describe('Component Tests', () => {

    describe('UserProfile Management Detail Component', () => {
        let comp: UserProfileDetailComponent;
        let fixture: ComponentFixture<UserProfileDetailComponent>;
        let service: UserProfileService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FyPv6TestModule],
                declarations: [UserProfileDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    UserProfileService,
                    JhiEventManager
                ]
            }).overrideTemplate(UserProfileDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserProfileDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserProfileService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new UserProfile(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.userProfile).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
