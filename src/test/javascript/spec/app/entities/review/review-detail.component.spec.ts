/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { FyPv6TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ReviewDetailComponent } from '../../../../../../main/webapp/app/entities/review/review-detail.component';
import { ReviewService } from '../../../../../../main/webapp/app/entities/review/review.service';
import { Review } from '../../../../../../main/webapp/app/entities/review/review.model';

describe('Component Tests', () => {

    describe('Review Management Detail Component', () => {
        let comp: ReviewDetailComponent;
        let fixture: ComponentFixture<ReviewDetailComponent>;
        let service: ReviewService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FyPv6TestModule],
                declarations: [ReviewDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ReviewService,
                    JhiEventManager
                ]
            }).overrideTemplate(ReviewDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReviewDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReviewService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Review(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.review).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
