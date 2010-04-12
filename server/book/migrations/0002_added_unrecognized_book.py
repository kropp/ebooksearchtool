
from south.db import db
from django.db import models
from book.models import *

class Migration:
    
    def forwards(self, orm):
        "Write your forwards migration here"
        Book.objects.create(title='__unrecognized_book_files__', \
                            language=Language.objects.get(short='?'))
    
    
    def backwards(self, orm):
        "Write your backwards migration here"
        Book.objects.get(title='__unrecognized_book_files__').delete()
    
    
    models = {
        'book.annotation': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.TextField', [], {'max_length': '10000'})
        },
        'book.author': {
            'alias': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.AuthorAlias']", 'null': 'True', 'blank': 'True'}),
            'credit': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '255'}),
            'tag': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Tag']", 'null': 'True', 'blank': 'True'})
        },
        'book.authoralias': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '255'})
        },
        'book.book': {
            'annotation': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Annotation']", 'null': 'True', 'blank': 'True'}),
            'author': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Author']", 'null': 'True', 'blank': 'True'}),
            'book_file': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.BookFile']"}),
            'credit': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'lang': ('django.db.models.fields.CharField', [], {'max_length': '2'}),
            'language': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['book.Language']"}),
            'series': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Series']", 'null': 'True', 'blank': 'True'}),
            'tag': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Tag']", 'null': 'True', 'blank': 'True'}),
            'title': ('django.db.models.fields.CharField', [], {'max_length': '255'})
        },
        'book.bookfile': {
            'credit': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'img_link': ('django.db.models.fields.TextField', [], {'max_length': '4000', 'null': 'True', 'blank': 'True'}),
            'last_check': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            'link': ('django.db.models.fields.TextField', [], {'max_length': '4000'}),
            'link_hash': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '32'}),
            'more_info': ('django.db.models.fields.TextField', [], {'max_length': '10000', 'null': 'True', 'blank': 'True'}),
            'size': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'time_found': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'type': ('django.db.models.fields.CharField', [], {'max_length': '10'})
        },
        'book.language': {
            'full': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '255'}),
            'full_national': ('django.db.models.fields.CharField', [], {'max_length': '255'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'short': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '2'})
        },
        'book.series': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '255'})
        },
        'book.tag': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '255'})
        }
    }
    
    complete_apps = ['book']
