/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { FyPv6TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { IngredientDetailComponent } from '../../../../../../main/webapp/app/entities/ingredient/ingredient-detail.component';
import { IngredientService } from '../../../../../../main/webapp/app/entities/ingredient/ingredient.service';
import { Ingredient } from '../../../../../../main/webapp/app/entities/ingredient/ingredient.model';

describe('Component Tests', () => {

    describe('Ingredient Management Detail Component', () => {
        let comp: IngredientDetailComponent;
        let fixture: ComponentFixture<IngredientDetailComponent>;
        let service: IngredientService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FyPv6TestModule],
                declarations: [IngredientDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    IngredientService,
                    JhiEventManager
                ]
            }).overrideTemplate(IngredientDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IngredientDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IngredientService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Ingredient(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.ingredient).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
